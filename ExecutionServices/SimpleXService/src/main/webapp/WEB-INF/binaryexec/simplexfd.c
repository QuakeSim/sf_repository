/* -------------------- simplex v 4.0 ------------------------------------ */
/*
   'simplex' is a program that inverts for multiple dislocation fault
   parameters, given surface displacement data.  The program is based
   on the downhill simplex optimization algorithm with the addition of
   simulated annealing, as published by Press et al. in Numerical Recipes
   3rd ed.  The program also incorporates the  implementation of elastic
   dislocations in the program 'disloc' by Andrea Donnellan, 10 May, 1989
*/
/*       - Revision History -

   v 0.5 - 1994-5 - early development by Floyd Ross and G. Lyzenga
   v 1.0 - May 96 - first working version by Wendy Panero
   v 1.1 - Jun 96 - modifications by G. Lyzenga to add relative displacements
   v 1.2 - Nov 96 - modifications by G. Lyzenga to add residual output
   v 1.3 - Jan 97 - modifications by G. Lyzenga to clean up output
   v 2.0 - Jan 97 - modifications by G. Lyzenga to revamp program structure
   v 3.0 - Apr 97 - revision by J. Parker to do covariances by finite diff
   v 4.0 - Apr 98 - added data types 4-6 (strain) and 7 (LOS displacement).
*/
/* ------------------------------------------------------------------------ */

#include   	<stdio.h>
#include   	<math.h>
#include    "ran.h"
#include    "math_tools_for_matrix_algebra.h"

#define  GET_PSUM \
                    for(n=0;n<ndim;n++) {\
                    for(sum=0.0,m=0;m<nverts;m++) sum += p[m*ndim+n] ; \
                    psum[n] = sum ; }
#define IX(i,j) ((j*(j+1))/2 + i)
#define  MAXFLTS  100
#define  MAXSOLV  100

double  typical_dist = 10.0 ;   /*  km  */
double  typical_slip = 1000.0 ;   /*  mm  */
double  fuzz = 0.05 ;
double  ddel = 1.e-5;
double  pi ;
double  FACTOR=0.1;  /* for temperature control */

struct data_point
   {
    int type;     /* 1 -> east  2 -> north  3 -> up  */
                  /* 4 -> exx, 5 -> exy, 6 -> eyy */
                  /* 7 -> LOS displacement in given el,az */
                  /* 8 -> fault soft constraint */
    int ifault;   /* matches the fault index (zero-based) of input list */
    int nparam;   /* matches flt parameter number, input list (one-based) */
    double  xloc ;  /* east location  */
    double  yloc ;  /* north location  */
    double  disp ;  /* displacement or strain  */
    double  unc ;  /* uncertainty  */
    double  resid ;  /* residual  */
    double  elevation; /* sar line of sight angle above horizon */
    double  azimuth;   /* sar line of sight angle west of north */ 
   } ;

struct sumout
   {
    double  d1,d2,d3;
    double str1,str2,str3;
   }*sum;

struct ffault
   {
   double  u[9];
   double e[18];
   double  up[9];
   double  ep[18];
   };

struct fltdata
   {
   double lambda;  /* These are the Lame constants */
   double mu;
   				  /* The following are the ten points of the simplex */
   double lat;	  /* This is really the x-coord of each fault */
   double lon;	  /* And the y-coord... */
   double strike; /* The strike... */
   double depth;  /* depth... */
   double delta;  /* dip */
   double len;	  /* length */
   double wth;	  /* width */
   double u1;	  /* strike-slip disp. */
   double u2;	  /* dip-slip disp. */
   double u3;	  /* tensile disp. */
   } *the_flt;

struct data_point  *obs ;
long  idum ;  /* random number seed */
double  negtemp ;
int  ndata , nfaults, numtrials;
long ndim , nverts , nupper;
int     lookup[MAXSOLV][2] ;
int     nactive[MAXFLTS][9] ;
double  guess[MAXFLTS][9] ;
double  outp_dis[6];
double  outp_str[6];

int  rel_flag ;   /*indicates whether absolute (0) or relative (1)
                    displacements are being fit */
int  rel_ref[3] ; /* index #'s of reference site */
int  ndata_actual ;

FILE  *in_fp , *out_fp, *fault_fp ;
/*
double chisq( double p[] , int flag ) ;
double gasdev (double sigma, long *idum);
void update_soln( double *p ) ;
void OutPut();
*/
void triangle_matrix_copy(double *a, double *b, long n);
int count;
int current=0;
int hold=-1;
int singular_return;
char outfault[100];

double row0[100], row1[100], row2[100];
double ave0, ave1, ave2;
double *origdata, *table;

/* *************************** begin main ************************** */

main(int argc,char *argv[])
{
 int  iter , i , j, k, l, nparam , iflt , ityp, temp, nsets;
 double  start_temp , ftol=1.e-10;
 double  *p, *pb, *pp, *p_1 , *psum , *ptry , *y , *pchi2 , *pmchi2 , *dchi2,
        *utility ;
 double  yb = 1.0e+12 ;
 double  y_1 ;
 void amebsa( double *p , double y[] ,
              double pb[] , double psum[] , double ptry[] , double *yb ,
              double (*funk)(double [] , int) , int *iter , double temptr , 
	          double ftol) ;
 double NewTemp(double yb,double start_temp, int iter);
 void OutPut();
 void GetStats (double *table,double yb,double pb[],double y[],double p[]) ;
 void OutPutCor2(double *correlation);
 void OutPutCov(double *correlation);
 void OutPutDev(double *stddev, double *average);
 double chisq( double p[] , int flag ) ;
 double gasdev (double sigma, long *idum);
 double *average, *stddev, *correlation;
 double chifac;
 

/*  First open the input and output files  */

 in_fp = fopen(argv[1],"r") ;
 out_fp = fopen(argv[2],"w") ;
 strcpy(outfault,argv[1]);
 strtok(outfault,".-_");
 strcat(outfault,".fault");
 printf("%s\n",outfault);
 

 pi = acos( -1.0 ) ;

/* Read in number of faults and params for each  */

 fscanf(in_fp,"%d",&nfaults) ;
 printf("# of faults = %d\n",nfaults) ;

 the_flt = (struct fltdata *) calloc(nfaults,sizeof(struct fltdata)) ;

 ndim = 0 ;
 for(i=0;i<nfaults;i++)
    {                              /* iiiiiiiiiii * loop i * iiiiiiiiiii */
     (the_flt+i)->lambda = 1.0 ;
     (the_flt+i)->mu     = 1.0 ;

     for(j=0;j<9;j++)
        {                       /* jjjjjjjjjjj * loop j * jjjjjjjjjjjjj */
         fscanf(in_fp, "%d", &nparam ) ;

        /*  parameter numbers (nparam):
            xloc=1 , yloc=2 , strike=3 , dip=4 , depth=5 ,
            width=6 , length=7 , ustrike=8 , udip=9 */

         fscanf(in_fp, "%d%lf", &nactive[i][nparam-1] ,
                 &guess[i][nparam-1] ) ;

         switch(nparam)
           {
            case 1:
               (the_flt+i)->lon = guess[i][nparam-1] ;
               break ;

            case 2:
               (the_flt+i)->lat = guess[i][nparam-1] ;
               break ;

            case 3:
               (the_flt+i)->strike = guess[i][nparam-1] ;
               break ;

            case 4:
               (the_flt+i)->delta = guess[i][nparam-1] ;
               break ;

            case 5:
               (the_flt+i)->depth = guess[i][nparam-1] ;
               break ;

            case 6:
               (the_flt+i)->wth = guess[i][nparam-1] ;
               break ;

            case 7:
               (the_flt+i)->len = guess[i][nparam-1] ;
               break ;

            case 8:
               (the_flt+i)->u1 = guess[i][nparam-1] ;
               break ;

            case 9:
               (the_flt+i)->u2 = guess[i][nparam-1] ;
               break ;
           }

         if(nactive[i][nparam-1])
           {
            /* Generate a lookup table for "active" parameters */
            lookup[ndim][0] = i ;  /* fault # */     
            lookup[ndim][1] = nparam ;  /* parameter type # */     
            ndim++ ;
           }
         }                       /* jjjjjjjjjjj * loop j * jjjjjjjjjjjjj */
     }                              /* iiiiiiiiiii * loop i * iiiiiiiiiii */
 nverts = ndim+1 ;

 printf("# of free parameters = %d\n",ndim) ;
 rel_flag = 0 ;  /* absolute is default */

/* Next read in the number of data pts and the data  */

 fscanf(in_fp,"%d",&ndata); /* version 2.0 only has one data set per file */
 ndata_actual = ndata ;
 

 fscanf(in_fp,"%lf%d%d",&start_temp,&iter,&numtrials) ;
 printf("# of data points = %d\n",ndata) ;

 obs = (struct data_point *) calloc(ndata, sizeof(struct data_point)) ;
 sum = (struct     sumout *) calloc(ndata, sizeof(struct     sumout)) ;
 origdata = (double *) calloc(ndata, sizeof(double));

 for(i=0;i<ndata;i++)
   {
    fscanf(in_fp,"%d", &obs[i].type );

/* slightly different for type 8 (constraints): integer fault and param, not 
   xloc and yloc */

    if(abs(obs[i].type) == 8)
      fscanf(in_fp, "%d%d%lf%lf", &((obs+i)->ifault) ,
           &((obs+i)->nparam) , &((obs+i)->disp) , &((obs+i)->unc) ) ;
    else
      fscanf(in_fp, "%lf%lf%lf%lf",  &((obs+i)->xloc) ,
           &((obs+i)->yloc) , &((obs+i)->disp) , &((obs+i)->unc) ) ;

    origdata[i] = ((obs+i)->disp);  /*keep a good copy of the displacements*/

    if(abs(obs[i].type)==7) 
       { 
         fscanf(in_fp,"%lf%lf",&((obs+i)->elevation) , &((obs+i)->azimuth) );
         obs[i].elevation *= pi/180.;
         obs[i].azimuth *= pi/180.;
       }
    if((obs+i)->type < 0)  /* negative type indicates a relative reference */
       {
        rel_flag = 1 ;  /* set to relative mode */
        ndata_actual-- ;
        j = -((obs+i)->type) - 1 ;
        rel_ref[j] = i ;    /* where to find the jth reference location */
       }
   }

/* override (jwp) because we no longer do trials, but 1 will preserve averages */ 
numtrials = 1;
 printf("temperature = %f , iterations = %d, 'trials' = %d\n",
         start_temp,iter, numtrials) ;

  idum = -55555L; /* Initialize the random number generator. */
  ran1(&idum);    /*returns a random number with idum as seed */
  idum = 12345L ;  /* and set to a positive seed!  */

/*  Allocate storage for the simplex ... */  

     /* each p consists of ndim normalized parameters */
  p = (double *) calloc (1,(size_t)((nverts*ndim)*sizeof(double)));
  p_1 = (double *) calloc (1,(size_t)(ndim*sizeof(double)));
  pb = (double *) calloc (1,(size_t)(ndim*sizeof(double)));
  pp = (double *) calloc (1,(size_t)(ndim*sizeof(double)));
  psum = (double *) calloc (1,(size_t)(ndim*sizeof(double)));
  ptry = (double *) calloc (1,(size_t)(ndim*sizeof(double)));

      /* each y value is a chi-square "quality"  */
  y = (double *) calloc (1,(size_t)(nverts*sizeof(double)));

      /* new (jwp) pchi2 holds perturbations, dchi2 2nd derivatives */
  nupper = (ndim*(ndim+1))/2;
  pchi2 = (double *) calloc (1,(size_t)(nupper*sizeof(double)));
  pmchi2 = (double *) calloc (1,(size_t)(nupper*sizeof(double)));
  dchi2 = (double *) calloc (1,(size_t)(nupper*sizeof(double)));
  utility = (double *) calloc (1,(size_t)(nupper*sizeof(double)));
  average = (double *) calloc (1,(size_t)((ndim)*sizeof(double)));
  stddev = (double *) calloc (1,(size_t)((ndim)*sizeof(double)));
  correlation = (double *) calloc (1,(size_t)((ndim*ndim)*sizeof(double)));
  
      /* table to collect trial results for covariance calculation */
  table = (double *) calloc (1,(size_t)((ndim*numtrials)*sizeof(double)));
  
/*  Now generate the initial simplex ... */

 for(i=0;i<ndim;i++)
     {                /* iiiiiiiiiii * loop i * iiiiiiiiiii */
      iflt = lookup[i][0] ;
      ityp = lookup[i][1] ;
      switch(ityp)
         {
          case 1:
          case 2:
                  p[i] = guess[iflt][ityp-1]/typical_dist ;
                  break ;
          case 3:
                  p[i] = tan(pi*guess[iflt][ityp-1]/360.0) ;
                  break ;
          case 4:
                  p[i] = tan((pi/360.0)*guess[iflt][ityp-1]) ;
                  break ;
          case 5:
          case 6:
          case 7:
                  p[i] = log(guess[iflt][ityp-1]/typical_dist) ;
                  break ;
          case 8:
          case 9:
                  p[i] = guess[iflt][ityp-1]/typical_slip ;
                  break ;
         }

      for(j=0;j<ndim;j++)
         {                  /* neighboring guesses w/"fuzz" factor */
          if(j == i)
             p[(j+1)*ndim+i] = p[i] + fuzz ;
          else
             p[(j+1)*ndim+i] = p[i] ;
         
         }
     }                /* iiiiiiiiiii * loop i * iiiiiiiiiii */

/* Next we must initialize the array of simplex function values  */

 printf("Simplex filled...\n") ;
 
/* print initial data residuals */
            chisq(p,1); 

 for(i=0;i<nverts;i++)
   {
    y[i] = chisq( &p[i*ndim] , 0 ) ;

    if(y[i] < yb){
      yb = y[i];
      for(j=0;j<ndim;j++)
        pb[j] = p[i*ndim+j];
    }
    yb = y[i] < yb? y[i]:yb;

    printf("chisq(%d) = %f\n",i,y[i]) ;
   }

 printf("functions initialized...\n") ;
 
 /*  beginning of actual solution algorithm... */

/* no longer need to loop over trials, just perturb final chisq */
	   
	    count=0;

	/* Now call the main simplex-SA routine  */
	
	    while (start_temp>=0)
          {     /*.... As long as the system isn't frozen...*/
	       printf ("start temp = %f\n", start_temp);
	       temp=iter;
	       amebsa( p , y , pb , psum , ptry , &yb , chisq , &iter ,
                   start_temp ,ftol) ;
	       iter=temp;
	       start_temp=NewTemp(yb, start_temp, iter);
	      }

	    printf("number of steps taken: %d\n", count);

/*  take out first printing of residuals (ad).  Move to later
            chisq(pb,1); */
	
	/*Put results in the table*/
	    for (i=0;i<ndim;i++)
		   table[i] = pb[i];
		   
        y_1 = yb ;
        for(i=0;i<ndim;i++)
         {
          p_1[i] = pb[i] ;
        }

/*  added here (ad) put back in output reporting of results */
/* jwp expanded OutPut to write table of faults to <basename>.fault  */

        OutPut();

	/* New stuff: (jwp) compute chisq for ndim(ndim+1)/2 perturbations */

        /* need to convert from chisq == chi squared per dof, to chi squared */
        chifac = (double) (ndata_actual - ndim);
        y_1 *= chifac;

        /* collect perturbations: positive (pchi2), then negative (pmchi2) */
        for(i=0;i<ndim;i++){
           for(j=i;j<ndim;j++){
              for(k=0;k<ndim;k++) pp[k]=pb[k];
              if(i==j) {
                 pp[i] = pb[i] + ddel;
              } else {
                 pp[i] = pb[i] + ddel;
                 pp[j] = pb[j] + ddel;
              }
              pchi2[IX(i,j)] = chisq(pp,0)*chifac;
         /*  printf("del-pchi2: %d %d %e\n", i, j,pchi2[IX(i,j)]-y_1 ); */
           } /* j=i . . . */
        }    /* i=0,. . . */

        for(i=0;i<ndim;i++){
           for(j=i;j<ndim;j++){
              for(k=0;k<ndim;k++) pp[k]=pb[k];
              if(i==j) {
                 pp[i] = pb[i] - ddel;
              } else {
                 pp[i] = pb[i] - ddel;
                 pp[j] = pb[j] - ddel;
              }
              pmchi2[IX(i,j)] = chisq(pp,0)*chifac;
           /*printf("del-pmchi2: %d %d %e\n", i, j,pmchi2[IX(i,j)]-y_1 ); */
           } /* j=i . . . */
        }    /* i=0,. . . */
       
        /* compute finite difference 2nd derivatives 
           (actually, below computes 0.5* chisq'');
 
     reference: 
           Abramowitz and Stegun, Handbook of Mathematical Functions,
           formula 25.3.27
        */

        for(i=0;i<ndim;i++){
           for(j=i;j<ndim;j++){
              if(i==j) {
                 dchi2[IX(i,j)] = (0.5/(ddel*ddel)) *
                  ( pmchi2[IX(i,i)] - 2*y_1 + pchi2[IX(i,i)] );
              } else {
                 dchi2[IX(i,j)] = ( -0.25/(ddel*ddel) ) * 
                  ( pchi2[IX(i,i)] + pchi2[IX(j,j)] + 
                    pmchi2[IX(i,i)] + pmchi2[IX(j,j)] 
                   - 2*y_1 - pchi2[IX(i,j)] - pmchi2[IX(i,j)] );
              }
           /*printf("dchi2: %d %d %f\n", i, j, dchi2[IX(i,j)] );*/

           } /* j=i . . . */
        }    /* i=0,. . . */

        /* change half-chisq'' matrix into  covariance. Use cholesky for error control */

        triangle_matrix_copy(dchi2,utility,ndim);
        singular_return = lower_cholesky_factor(utility,utility,ndim);
        if(singular_return != -1){
           printf("Matrix factorization failed at row %d\n",singular_return);
/* added (ad) */
           chisq(pb,1);
           fprintf(out_fp, "\n\n******* best-ever solution ********\n");
           fprintf(out_fp, "best-ever chi-square (yb) = %g\n",yb);

           printf("\n\n******* best-ever solution ********\n");
           printf("best-ever chi-square (yb) = %g\n",yb);
           exit(-1);
        }
        singular_return = invert_upper_triangle_matrix(utility,utility,ndim);
        if(singular_return != 0){
           printf("Matrix inversion failed at row %d\n",singular_return);
/* added (ad) */
           chisq(pb,1);
           fprintf(out_fp, "\n\n******* best-ever solution ********\n");
           fprintf(out_fp, "best-ever chi-square (yb) = %g\n",yb);

           printf("\n\n******* best-ever solution ********\n");
           printf("best-ever chi-square (yb) = %g\n",yb);
           exit(-1);
        }
        square_upper_triangle_matrix(utility,utility,ndim);
        inverse_check(dchi2,utility,ndim);


        for(i=0;i<ndim;i++)
        {
           stddev[i] = sqrt(utility[IX(i,i)]);
           average[i] = p[i];
        }

        OutPutDev(stddev,average);

        for(i=0;i<ndim;i++){
          for(j=i;j<ndim;j++)
            correlation[IX(i,j)] = utility[IX(i,j)]/stddev[i]/stddev[j];
        }
        OutPutCor2(correlation);
/* redefine stddev to be in the fault-parameter system, unscaled */
 for(i=0;i<ndim;i++)
    {
     iflt = lookup[i][0] ;
     ityp = lookup[i][1] ;

     switch(ityp)
           {
            case 1:
            case 2:
               stddev[i] *= typical_dist ;
               break ;

            case 3:
               stddev[i] *= (360.0/pi)/(1+average[i]*average[i]) ;
               break ;

            case 4:
               stddev[i] *= (360.0/pi)/(1+average[i]*average[i]);
               break ;

            case 5:
            case 6:
            case 7:
               stddev[i] *=  exp(average[i]) * typical_dist;
               break ;

            case 8:
            case 9:
               stddev[i] *= typical_slip ;
               break ;
           }
    }
        for(i=0;i<ndim;i++){
          for(j=i;j<ndim;j++)
            utility[IX(i,j)] = correlation[IX(i,j)]*stddev[i]*stddev[j];
        }
        OutPutCov(utility);

/* added (ad) */
            chisq(pb,1);

            fprintf(out_fp, "\n\n******* best-ever solution ********\n");
            fprintf(out_fp, "best-ever chi-square (yb) = %g\n",yb);

            printf("\n\n******* best-ever solution ********\n");
            printf("best-ever chi-square (yb) = %g\n",yb);

    exit(0) ;

}/* *************************** end main ************************** */

/* *************************** begin amebsa ************************** */
void amebsa( double *p , double y[] ,
             double pb[] , double psum[] , double ptry[] , double *yb ,
             double (*funk)(double z[] , int flag) , int *iter , double temptr , 
	         double ftol)
{
  double amotsa(double *p,double y[],double psum[],double ptry[],
                double pb[],double *yb,double (*funk)(double [] , int),
                int ihi,double *yhi,double fac , double negtemp);
  int i,ihi,ilo,j,m,n, found, initial_iter,printed_count_hundred;
  double rtol,sum,swap,yhi,ylo,ynhi,ysave,yt,ytry ;

  found=0;
  initial_iter = *iter;
  printed_count_hundred = 0;
  negtemp = -temptr;
  GET_PSUM

  do {       /* *********** begin big do-while loop ********** */
    
   /*  monitor the progress...*/
      if ((initial_iter - *iter - printed_count_hundred) >= 100)
       {
        printf ("count = %d, chi-squared per dof = %f\n", 
                        count =  initial_iter - *iter,     *yb     );
        printed_count_hundred = 100*(count/100);
       }

      ilo=0;  /*Determine which point is the hightest (worst),*/
      ihi=1;   /*next highest, and lowest (best)*/

      /*Whenever we "look at" a vertex, it gets a random thermal fluctuation*/
      
      ynhi=ylo=y[0]+negtemp*log(ran1(&idum));  
      yhi=y[1]+negtemp*log(ran1(&idum));   
     
      if (ylo > yhi) {	
		ilo=1;	
		ihi=0;	
		ynhi=yhi;
		yhi=ylo;	
		ylo=ynhi;
     }

      /*Loop over the points in the simplex*/
      for (i=2;i<nverts;i++)
         { 
		  yt=y[i]+log(ran1(&idum))*negtemp;  /*More thermal fluctuations*/
	      if (yt <= ylo)
            {
	         ilo=i;       /*hold the vertex with best value*/
	         ylo=yt;      /*hold that value*/
	        }
	      if (yt > yhi)
            {
	         ynhi=yhi;
	         ihi=i;      /*hold vertex with worst...*/
	         yhi=yt;
	        }
	      else if (yt > ynhi)
	        ynhi=yt;
         }



      rtol=2.0*fabs(yhi-ylo)/(fabs(yhi)+fabs(ylo));
      
      /*Compute the fractional range from highest to lowest and return if 
	    satisfactory*/

      if (*iter < 0  ||   rtol < ftol )
	    {             /*If returning, put best point and value in slot 1*/
            if(*iter < 0) printf("Amebsa has done all requested iterations.\n");
            if(rtol<ftol) printf("Amebsa has met convergence criterion.\n");
	     found=1;
	     swap=y[0];
	     y[0]=y[ilo];
	     y[ilo]=swap;
	     for (n=0;n<ndim;n++)
            {
	         swap=p[n];
	         p[n]=p[ilo*ndim+n];
	         p[ilo*ndim+n]=swap;
	        }
	    }

/*Begin a new iteration.  First extrapolate by a factor -1 through the face of 
the simplex across from the highest point, i.e., reflect the simplex from the 
highest point*/

      else     /* not returning yet...  */
        {
	     *iter -= 2;

	     ytry=amotsa(p,y,psum,ptry,pb,yb,funk,ihi,&yhi,-1.0 , negtemp);
        /*reflect worst*/

	     if (ytry <= ylo)

	  /*Gives a result better than the best point, so try an additional 
	    extrapolation by a factor of 2. */

	     ytry=amotsa(p,y,psum,ptry,pb,yb,funk,ihi,&yhi,2.0, negtemp);

	     else
            {
	         if (ytry >= ynhi)
                {    /*halve, shrink*/
	             ysave=yhi;
	             ytry=amotsa(p,y,psum,ptry,pb,yb,funk,ihi,&yhi,0.5 , negtemp);

	             if (ytry >= ysave) 
	               {      /*Can't seem to improve on highest,
                           so contract around lowest*/
		            for (i=0;i<nverts;i++) 
		               {
		                if (i != ilo) 
		                  {
			               for (j=0;j<ndim;j++) 
			                  {
			        psum[j]=0.5*(p[i*ndim+j]+p[ilo*ndim+j]);
                   /*shrink here*/
			                   p[i*ndim+j]=psum[j];
			                  }
			  
			               y[i]=(*funk)(psum,0);           /*new chi-sqr*/
			
		                  }
		               }
		            *iter -= ndim;
		            GET_PSUM  /*Recompute psum*/
	               }
	            }

	         else
               ++(*iter);  /*correct the evaluation count*/
	        }
        }


    } while (!found); /* *********** end big do-while loop ********** */


} /* *************************** end amebsa ************************** */


 /* *************************** begin amotsa ************************** */

double amotsa(double *p,double y[],double psum[],double ptry[],
                double pb[],double *yb,double (*funk)(double z[] , int flag),
                int ihi,double *yhi,double fac, double negtemp)

/*Extrapolates by a factor fac though the face of the simplex across from the
high point, tries it, and replaces the high point if the new point is better*/

{
  int 	j;
  double fac1,fac2,yflu,ytry;

  fac1=(1.0-fac)/ndim;
  fac2=fac1-fac;

  for (j=0;j<ndim;j++)
     {
      ptry[j]=psum[j]*fac1-p[ihi*ndim+j]*fac2;
     }
  
  ytry=(*funk)(ptry,0);

  if (ytry <= *yb)
    {                         /*Save the best-ever*/
     for (j=0;j<ndim;j++) pb[j]=ptry[j];
     *yb=ytry;
    }

  yflu=ytry-negtemp*log(ran1(&idum));

  if (yflu < *yhi)
    {
     y[ihi]=ytry;
     *yhi=yflu;
     for (j=0;j<ndim;j++)
        {
         psum[j] += ptry[j]-p[ihi*ndim+j];
         p[ihi*ndim+j]=ptry[j];
        }
    }

  return yflu;

} /* *************************** end amotsa ************************** */

 /* *************************** begin NewTemp ************************** */

double NewTemp(double yb, double start_temp, int iter)
{
 if (yb <= 0.01 || start_temp <= 0.0)
	start_temp = -1.0 ;
 else if (start_temp < (0.1))
    start_temp = 0.0;
 else 
    start_temp *= 1.-FACTOR;
    start_temp -= FACTOR;

 return (start_temp);

} /* *************************** end NewTemp ************************** */

/* *************************** begin chisq ************************** */

double chisq ( double p[] , int print_flag )
{
 int i , k , n , iflt , ityp ;
 int ignore ;
 double value = 0.0 ;
 double  xx , yy , xpt , ypt , calc , observ , uncert ;
 double  sa,ca,sd,cd,mat;
 struct sumout *tSum;
 void fault(int,int,double,double,double,double,double,double,double);
 void update_soln( double *p ) ;

 if( print_flag )
    {
     fprintf(out_fp, "\nResidual displacements:\n");
     fprintf(out_fp,
         "type\t\txloc\t\tyloc\t\tobserv\t\tcalc\t\to-c\t\terror\n\n");
    }
 tSum = sum;
 for(i=0;i<ndata;i++)  /* first clear the sum buffers */
    {
     tSum->d1 = 0.0 ;
     tSum->d2 = 0.0 ;
     tSum->d3 = 0.0 ;
     tSum->str1 = 0.0 ;
     tSum->str2 = 0.0 ;
     tSum->str3 = 0.0 ;
     tSum++;
    }
 /* update the varying parameters */

 update_soln(p) ;

 for(i=0;i<nfaults;i++)  /* loop over the faults */
    {

       /* set fault consts */
      sa = sin(pi*((the_flt+i)->strike - 90.0)/180.0);
      ca = cos(pi*((the_flt+i)->strike - 90.0)/180.0);
      cd = cos((the_flt+i)->delta * pi/180.0);
      if (fabs(cd) <= 1.0e-08) cd = 0.0;
      sd = sin((the_flt+i)->delta * pi/180.0);
      mat = (the_flt+i)->mu/((the_flt+i)->lambda + (the_flt+i)->mu);

     for(k=0;k<ndata;k++)  /* loop over the data points */
        {
         xx = (obs+k)->xloc - (the_flt+i)->lon;
         yy = (obs+k)->yloc - (the_flt+i)->lat;
	     xpt = xx*ca-yy*sa;
	     ypt = xx*sa+yy*ca;


         fault(i,k,xpt,ypt,sa,ca,sd,cd,mat) ;  /* call disloc routine */

        }
    }

 for(k=0;k<ndata;k++)  /* loop over the data points */
    {
     ignore = 0 ;
     switch((obs+k)->type)
        {
         case 1:
            if(rel_flag)
               {
                i = rel_ref[0] ;
                calc = (sum+k)->d1 - (sum+i)->d1 ;
               }
            else
               calc = (sum+k)->d1 ;
            break ;

         case 2:
            if(rel_flag)
               {
                i = rel_ref[1] ;
                calc = (sum+k)->d2 - (sum+i)->d2 ;
               }
            else
               calc = (sum+k)->d2 ;
            break ;

         case 3:
            if(rel_flag)
               {
                i = rel_ref[2] ;
                calc = (sum+k)->d3 - (sum+i)->d3 ;
               }
            else
               calc = (sum+k)->d3 ;
            break ;

         case 4:
            if(rel_flag)
               {
                i = rel_ref[3] ;
                calc = (sum+k)->str1 - (sum+i)->str1 ;
               }
            else
               calc = (sum+k)->str1 ;
            break ;

         case 5:
            if(rel_flag)
               {
                i = rel_ref[4] ;
                calc = (sum+k)->str2 - (sum+i)->str2 ;
               }
            else
               calc = (sum+k)->str2 ;
            break ;

         case 6:
            if(rel_flag)
               {
                i = rel_ref[5] ;
                calc = (sum+k)->str3 - (sum+i)->str3 ;
               }
            else
               calc = (sum+k)->str3 ;
            break ;
         case 7:
            if(rel_flag)
              {
               i = rel_ref[6] ;
               calc = (sum[k].d1 - sum[i].d1) *
                         cos(obs[k].azimuth) * cos(obs[k].elevation) +
                      (sum[k].d2 - sum[i].d2) *
                         sin(obs[k].azimuth) * cos(obs[k].elevation) +
                      (sum[k].d3 - sum[i].d3) * sin(obs[k].elevation);
               }
            else
               calc = sum[k].d1*cos(obs[k].azimuth)*cos(obs[k].elevation) +
                      sum[k].d2*sin(obs[k].azimuth)*cos(obs[k].elevation) +
                      sum[k].d3*sin(obs[k].elevation);
            break ;
         case 8:
	    switch(obs[k].nparam)
	       {
	       case 1:
                  calc = the_flt[obs[k].ifault].lon;
                  break;
	       case 2:
                  calc = the_flt[obs[k].ifault].lat;
                  break;
	       case 3:
                  calc = the_flt[obs[k].ifault].strike;
                  if(calc-(obs+k)->disp > 180.) calc = calc-360.;
                  if(calc-(obs+k)->disp < -180.) calc = calc+360.;
                  break;
	       case 4:
                  calc = the_flt[obs[k].ifault].delta;
                  if(calc-(obs+k)->disp > 180.) calc = calc-360.;
                  if(calc-(obs+k)->disp < -180.) calc = calc+360.;
                  break;
	       case 5:
                  calc = the_flt[obs[k].ifault].depth;
                  break;
	       case 6:
                  calc = the_flt[obs[k].ifault].wth;
                  break;
	       case 7:
                  calc = the_flt[obs[k].ifault].len;
                  break;
	       case 8:
                  calc = the_flt[obs[k].ifault].u1;
                  break;
	       case 9:
                  calc = the_flt[obs[k].ifault].u2;
                  break;
               default:
                  printf("type error: shouldn't get here. . .\n");
                  break;
             } 
            break;

         default:
            ignore = 1 ;
            break ;
        }
     if( ignore )  continue ;
     observ = (obs+k)->disp ;
     uncert = (obs+k)->unc ;
     (obs+k)->resid = observ - calc ;
     value += (observ-calc)*(observ-calc)/(uncert*uncert) ;
     
     /*  optional residual printing here...  */

        if( print_flag )
           {
            if(obs[k].type == 8)
              fprintf(out_fp,
                "%3d\t%3d\t%3d\t%10.4g\t%10.4g\t%10.4g\t%10.4g\n",
                obs[k].type, obs[k].ifault, obs[k].nparam,
                observ, calc, obs[k].resid,uncert );
            else
              fprintf(out_fp, 
                "%3d\t%10.4f\t%10.4f\t%10.4g\t%10.4g\t%10.4g\t%10.4g\n",
                (obs+k)->type , (obs+k)->xloc , (obs+k)->yloc ,
                observ , calc , (obs+k)->resid, uncert );
           }
 
    }
 value /= (double) (ndata_actual - ndim) ; /* chi-squared per dof */
 return( value ) ;
}
/* *************************** end chisq ************************** */

/* *************************** begin update_soln *********************** */

void update_soln( double *p )
{
 int  i , iflt , ityp ;

	for(i=0;i<ndim;i++)	
	   {	
	   iflt = lookup[i][0] ;
	   ityp = lookup[i][1] ;
	         switch(ityp)
	           {
	            case 1:
	               (the_flt+iflt)->lon =  p[i] * typical_dist ;
	               break ;
	
	            case 2:
	               (the_flt+iflt)->lat =  p[i] * typical_dist ;
	               break ;
	
	            case 3:
	               (the_flt+iflt)->strike = atan(p[i]) * 360.0/pi ;
	               break ;
	
	            case 4:
	               (the_flt+iflt)->delta = atan(p[i]) * 360.0/pi ;
	               break ;

                case 5:
                   (the_flt+iflt)->depth =  exp(p[i]) * typical_dist ;
                   break ;

                case 6:
                   (the_flt+iflt)->wth =  exp(p[i]) * typical_dist ;
                   break ;

                case 7:
                   (the_flt+iflt)->len =  exp(p[i]) * typical_dist ;
                   break ;

                case 8:
                   (the_flt+iflt)->u1 =  p[i] * typical_slip ;
                   break ;

                case 9:
                   (the_flt+iflt)->u2 =  p[i] * typical_slip ;
                   break ;
               }
	    }
	}
/* *************************** end update_soln *********************** */

/*********************************************************************/

void fault(int ifault,int ipoint,double xpt,double ypt,double sa,
      double ca,double sd,double cd,double mat)
{
   struct  ffault f;
   int     i,jj;
   double  xi,eta;
   double  p,upr[6],epr[18],xpr,ypr;
   void chin(int,double,double,double,double,double,double,struct ffault *);
   void transform(double *e,double *epr,double ca, double sa);

   p = ypt*cd+(the_flt+ifault)->depth*sd;

   for (i=0; i<9; ++i) f.u[i]=0.0;
   for (i=0; i<18; ++i) f.e[i]=0.0;

   eta = p;
   chin(ifault,sd,cd,mat,xpt,ypt,eta,&f);

   for (i=0; i<9; ++i) 
      f.u[i]=f.u[i]+f.up[i];
   for (i=0; i<18; ++i)
      f.e[i]=f.e[i]+f.ep[i];
   eta = p-(the_flt+ifault)->wth;
   chin(ifault,sd,cd,mat,xpt,ypt,eta,&f);

   for (i=0; i<9; ++i) 
      f.u[i]=f.u[i]-f.up[i];
   for (i=0; i<18; ++i)
      f.e[i]=f.e[i]-f.ep[i];

   xi = xpt-(the_flt+ifault)->len; eta = p;
   chin(ifault,sd,cd,mat,xi,ypt,eta,&f);

   for (i=0; i<9; ++i) 
      f.u[i]=f.u[i]-f.up[i];
   for (i=0; i<18; ++i)
      f.e[i]=f.e[i]-f.ep[i];

   xi = xpt-(the_flt+ifault)->len; eta = p-(the_flt+ifault)->wth;
   chin(ifault,sd,cd,mat,xi,ypt,eta,&f);

   for (i=0; i<9; ++i) 
      f.u[i]=f.u[i]+f.up[i];
   for (i=0; i<18; ++i)
      f.e[i]=f.e[i]+f.ep[i];

   for (i=0; i < 9; ++i) 
      if (fabs(f.u[i]) <= 1.0e-08) 
	    f.u[i] = 0.0;

   upr[0] = f.u[0]*ca+f.u[1]*sa;    upr[1] = -f.u[0]*sa+f.u[1]*ca;
   upr[2] = f.u[3]*ca+f.u[4]*sa;    upr[3] = -f.u[3]*sa+f.u[4]*ca;
   upr[4] = f.u[6]*ca+f.u[7]*sa;    upr[5] = -f.u[6]*sa+f.u[7]*ca;
   for (i=0; i < 6; ++i)
      if (fabs(upr[i]) <= 1.0e-08)
	    upr[i] = 0.0;
   transform(f.e,epr,ca,sa);
   for (i=0; i < 18; ++i)
      if (fabs(epr[i]) <= 1.0e-08)
         epr[i] = 0.0;

   outp_dis[0] = upr[0]+upr[2]+upr[4];
   outp_dis[1] = upr[1]+upr[3]+upr[5];
   outp_dis[2] = f.u[2]+f.u[5]+f.u[8];

   outp_str[0] = epr[0]+epr[4]+epr[8];
   outp_str[1] = 0.5*(epr[1]+epr[5]+epr[9]+epr[2]+epr[6]+epr[10]);
   outp_str[2] = epr[3]+epr[7]+epr[11];

   (sum+ipoint)->d1 += outp_dis[0];
   (sum+ipoint)->d2 += outp_dis[1];
   (sum+ipoint)->d3 += outp_dis[2];

   (sum+ipoint)->str1 += outp_str[0];
   (sum+ipoint)->str2 += outp_str[1];
   (sum+ipoint)->str3 += outp_str[2];
   
}

/*********************************************************************/
void chin(int ifault,double sd,double cd,double mat,double xi,double ypt,double eta,struct ffault *f)
{

   int     i;
   double  p,q,yt,dt,rr,xx,mat2,t1,t2,t3;
   double  at,i1,i2,i3,i4,i5,rdt,rdt2,jnk,jnk2,lg;
   double  j1,j2,j3,j4,axi,aeta,k1,k2,k3;
   double  frpe,xi2,xi3,rr3,rpe;

   p = ((ypt*cd) + ((the_flt+ifault)->depth * sd));
   q = ((ypt*sd) - ((the_flt+ifault)->depth * cd));
   yt = eta*cd + q*sd;
   dt = eta*sd - q*cd;
   rr = sqrt(xi*xi + eta*eta + q*q);
   xx = sqrt(xi*xi + q*q);
   mat2 = mat/2.0;
   t1 = -(the_flt+ifault)->u1/(2.0*pi);
   t2 = -(the_flt+ifault)->u2/(2.0*pi);
   t3 = 0.0 ;  /* assuming no tensile! */
   rdt = rr+dt; rdt2 = rdt*rdt;
   xi2 = xi*xi; xi3 = xi*xi*xi; rr3 = rr*rr*rr; rpe = rr*(rr+eta);
   frpe = (fabs(rr+eta) <= 1.0e-08) ? 0.0 : rr+eta;
/*
 *  find displacement parts
 */
   jnk2 = (xi*eta)/(q*rr);
   at = (fabs(q) <= 1.0e-08) ? 0.0 : atan(jnk2);
   lg = (frpe == 0.0) ? -log(rr-eta) : log(rr+eta);
   if (cd == 0.0)
      {
      i1 = -mat2*xi*q/rdt2;
      i3 = mat2*(eta/rdt+yt*q/rdt2-lg);
      i4 = -mat*q/rdt;
      i5 = -mat*xi*sd/(rr+dt);
      i2 = mat*(-lg)-i3;
      }
   else
      {
      jnk = (eta*(xx+q*cd)+xx*(rr+xx)*sd)/(xi*(rr+xx)*cd);
      i5 = (fabs(xi) <= 1.0e-08) ? 0.0 : (mat*2.0/cd)*atan(jnk);
      i4 = mat*(1.0/cd)*(log(rdt)-sd*lg);
      i3 = mat*((1.0/cd)*yt/rdt-lg)+sd*i4/cd;
      i2 = mat*(-lg)-i3;
      i1 = mat*(((-1.0)/cd)*(xi/rdt))-sd*i5/cd;
      }
   if (frpe == 0.0)
      {
      f->up[0] = t1*(at+i1*sd);
      f->up[1] = t1*(i2*sd);
      f->up[2] = t1*(i4*sd);
      f->up[3] = t2*(q/rr - i3*sd*cd);
      f->up[4] = t2*((yt*q)/(rr*(rr+xi))+cd*at-i1*sd*cd);
      f->up[5] = t2*((dt*q)/(rr*(rr+xi))+sd*at-i5*sd*cd);
      f->up[6] = t3*(-i3*sd*sd);
      f->up[7] = t3*((-dt*q)/(rr*(rr+xi))-sd*(-at)-i1*sd*sd);
      f->up[8] = t3*((yt*q)/(rr*(rr+xi))+cd*(-at)-i5*sd*sd);
      }
   else
      {
      f->up[0] = t1*((xi*q)/(rr*(rr+eta))+at+i1*sd);
      f->up[1] = t1*((yt*q)/(rr*(rr+eta))+(q*cd)/(rr+eta)+i2*sd);
      f->up[2] = t1*((dt*q)/(rr*(rr+eta))+(q*sd)/(rr+eta)+i4*sd);
      f->up[3] = t2*(q/rr - i3*sd*cd);
      f->up[4] = t2*((yt*q)/(rr*(rr+xi))+cd*at-i1*sd*cd);
      f->up[5] = t2*((dt*q)/(rr*(rr+xi))+sd*at-i5*sd*cd);
      f->up[6] = t3*((q*q)/(rr*(rr+eta))-i3*sd*sd);
      f->up[7] = t3*((-dt*q)/(rr*(rr+xi))-sd*((xi*q)/(rr*(rr+eta))-at)-i1*sd*sd);
      f->up[8] = t3*((yt*q)/(rr*(rr+xi))+cd*((xi*q)/(rr*(rr+eta))-at)-i5*sd*sd);
      }
/*
 *  find strain parts
 */
   if (cd == 0.0)
      {
      k1 = mat*xi*q/(rr*rdt2);
      k3 = mat*sd/rdt*(xi*xi/(rr*rdt)-1.0);
      k2 = (frpe == 0.0) ? (mat*(-sd/rr)-k3) :
                           (mat*(-sd/rr+q*cd/(rr*(rr+eta)))-k3);
      }
   else
      {
      k3 = (frpe == 0.0) ? (mat/cd*(-yt/(rr*rdt))) :
                           (mat/cd*(q/(rr*(rr+eta))-yt/(rr*rdt)));
      k2 = (frpe == 0.0) ? (mat*(-sd/rr)-k3) :
                           (mat*(-sd/rr+q*cd/(rr*(rr+eta)))-k3);
      k1 = (frpe == 0.0) ? (mat*xi/cd*(1.0/(rr*rdt))):
                           (mat*xi/cd*(1.0/(rr*rdt)-sd/(rr*(rr+eta))));
      }
   j1 = (cd == 0.0) ? (mat2*q/rdt2*(2.0*xi*xi/(rr*rdt)-1.0)):
                      (mat/cd*(xi*xi/(rr*rdt2)-1.0/rdt)-sd*k3/cd);
   j2 = (cd == 0.0) ? (mat2*xi*sd/rdt2*(2.0*q*q/(rr*rdt)-1.0)):
                      (mat/cd*(xi*yt/(rr*rdt2))-sd*k1/cd);
   j3 = (frpe == 0.0) ? (-j2): (mat*(-xi/(rr*(rr+eta)))-j2);
   j4 = (frpe == 0.0) ? (mat*(-cd/rr)-j1): (mat*(-cd/rr-q*sd/(rr*(rr+eta)))-j1);
   axi = (2.0*rr+xi)/(rr*rr*rr*(rr+xi)*(rr+xi));
   aeta = (frpe == 0.0) ? 0.0 : (2.0*rr+eta)/(rr*rr*rr*(rr+eta)*(rr+eta));
   f->ep[0] = -t1*(xi2*q*aeta-j1*sd);
   f->ep[1] = -t1*(xi3*dt/(rr3*(eta*eta+q*q))-(xi3*aeta+j2)*sd);
   f->ep[2] = -t1*(xi*q*cd/rr3+(xi*q*q*aeta-j2)*sd);
   f->ep[3] = (frpe == 0.0) ? -t1*(yt*q*cd/rr3+(q*q*q*aeta*sd-(xi2+
                                   (eta*eta))*cd/rr3- j4)*sd):
                              -t1*(yt*q*cd/rr3+(q*q*q*aeta*sd-2.0*q*sd/rpe-
                                   (xi2+(eta*eta))*cd/rr3-j4)*sd);
   f->ep[4] = -t2*(xi*q/rr3+j3*sd*cd);
   f->ep[5] = -t2*(yt*q/rr3-sd/rr+j1*sd*cd);
   f->ep[6] = (frpe == 0.0) ? -t2*(yt*q/rr3+j1*sd*cd):
                              -t2*(yt*q/rr3+q*cd/rpe+j1*sd*cd);
   f->ep[7] = (frpe == 0.0) ? -t2*(yt*yt*q*axi-(2.0*yt/(rr*(rr+xi)))*sd+
                                   j2*sd*cd):
                              -t2*(yt*yt*q*axi-(2.0*yt/(rr*(rr+xi))+xi*cd/rpe)
                                   *sd+j2*sd*cd);
   f->ep[8] = -t3*(xi*q*q*aeta+j3*sd*sd);
   f->ep[9] = -t3*(-dt*q/rr3-xi2*q*aeta*sd+j1*sd*sd);
   f->ep[10] = -t3*(q*q*cd/rr3+q*q*q*aeta*sd+j1*sd*sd);
   f->ep[11] = -t3*((yt*cd-dt*sd)*q*q*axi-q*2.0*sd*cd/(rr*(rr+xi))-
                     (xi*q*q*aeta-j2)*sd*sd);
   f->ep[12] = -t1*(-xi*q*q*aeta*cd+(xi*q/rr3-k1)*sd);
   f->ep[13] = -t1*(dt*q*cd/rr3+(xi*xi*q*aeta*cd-sd/rr+yt*q/rr3-k2)*sd);
   f->ep[14] = (frpe == 0.0) ? -t2*(dt*q/rr3+k3*sd*cd):
                               -t2*(dt*q/rr3+q*sd/rpe+k3*sd*cd);
   f->ep[15] = (frpe == 0.0)? -t2*(yt*dt*q*axi-2.0*dt/(rr*(rr+xi))*sd+k1*sd*cd):
                              -t2*(yt*dt*q*axi-(2.0*dt/(rr*(rr+xi))+xi*sd/rpe)*
                                    sd+k1*sd*cd);
   f->ep[16] = -t3*(q*q*sd/rr3-q*q*q*aeta*cd+k3*sd*sd);
   f->ep[17] = -t3*((yt*sd+dt*cd)*q*q*axi+xi*q*q*aeta*sd*cd-(2.0*q/(rr*(rr+xi))-
                     k1)*sd*sd);
}
/*********************************************************************/

/*********************************************************************/
void OutPut()
{   
 int i;

 fault_fp = fopen(outfault,"w") ;
 for(i=0;i<nfaults;i++)
    {
     fprintf(out_fp,"\n  Fault #%d\n",i+1) ;	 
     fprintf(out_fp,"x = %f\n",(the_flt+i)->lon) ;
     fprintf(out_fp,"y = %f\n",(the_flt+i)->lat) ;
     fprintf(out_fp,"strike = %f\n",(the_flt+i)->strike) ;
     fprintf(out_fp,"dip = %f\n",(the_flt+i)->delta) ;
     fprintf(out_fp,"depth = %f\n",(the_flt+i)->depth) ;
     fprintf(out_fp,"width = %f\n",(the_flt+i)->wth) ;
     fprintf(out_fp,"length = %f\n",(the_flt+i)->len) ;
     fprintf(out_fp,"strike-slip = %f\n",(the_flt+i)->u1) ;
     fprintf(out_fp,"dip-slip = %f\n",(the_flt+i)->u2) ;

     fprintf(fault_fp,"%d\t%f\t%f\t%f\t%f\t%f\t%f\t%f\t%f\t%f\n",
            i+1,the_flt[i].lon,the_flt[i].lat,the_flt[i].strike,
            the_flt[i].delta,the_flt[i].depth,the_flt[i].wth,
            the_flt[i].len,the_flt[i].u1,the_flt[i].u2);
    }
  fclose(fault_fp); 
}

/*********************************************************************/

/*********************************************************************/
void GetStats (double *table,double yb,double pb[],double y[],double p[])
{
 double *average, *stddev, *correlation;
 int i, j, k;   /* i will always go over the numtrials, j and k will always go over ndim*/
 double sum1 = 0.0, sum2 = 0.0, sum3 = 0.0;
 double  yave ;
 void update_soln( double *p ) ;
 void OutPut();
 void OutPutDev(double *stddev, double *average);
 void OutPutCor2(double *correlation);

 average = (double *) calloc (1,(size_t)((ndim)*sizeof(double)));
 stddev = (double *) calloc (1,(size_t)((ndim)*sizeof(double)));
 correlation = (double *) calloc (1,(size_t)((ndim*ndim)*sizeof(double)));
  
 for (j=0;j<ndim;j++)
	{
	 for (i=0;i<numtrials;i++)
		  sum1 += table[(i*ndim)+j];	
	 average[j] = sum1/numtrials;
	 printf("average[%d] = %e\n", j, average[j]);
	
	 for (i=0; i<numtrials; i++)
		  sum2 += (table[(i*ndim)+j]-average[j])*(table[(i*ndim)+j]-average[j]);
	 stddev[j] = sqrt(sum2/(numtrials));
	 printf("stddev[%d] = %e\n", j, stddev[j]);
	 sum1 = sum2 = 0.0 ;
	}
 for (j=0;j<ndim;j++)	
	for (k=0;k<=j;k++)
		{
		 for (i=0;i<numtrials;i++)
			sum3+= (table[(i*ndim)+j]-average[j])*(table[(i*ndim)+k]-average[k]);
		 correlation[ndim*j+k] = sum3/(numtrials*stddev[j]*stddev[k]);
		 sum3=0.0;
		}

/* reload the data slots with the original data */

        for(i=0;i<ndata;i++)
           {
            ((obs+i)->disp) = origdata[i] ;
           }
           
fprintf(out_fp, "\n\n******* best-ever solutions ********\n");
fprintf(out_fp, "best-ever chi-square (yb) = %f\n",yb);
update_soln(&pb[0]) ;
OutPut();
fprintf(out_fp, "\n\n************************************\n");

/*  take out trial printouts (ad) 

fprintf(out_fp, "\n\n******* last trial solutions ********\n");
fprintf(out_fp, "last trial chi-square (y1) = %f\n",y[0]);
update_soln(&p[ndim]) ;
OutPut();
fprintf(out_fp, "\n\n************************************\n");


 yave = chisq( &average[0] , 0 ) ;
fprintf(out_fp, "\n\n******* statistical average solutions ********\n");
fprintf(out_fp, "stat ave chi-square (yave) = %f\n",yave);
update_soln(&average[0]);
OutPut();
fprintf(out_fp, "\n\n************************************\n");

 update_soln(&stddev[0]);
 OutPutDev(stddev, average);
 OutPutCor2(correlation);
 
*/

 /* print out residual displacements 
 update_soln(&pb[0]) ; */
 yave = chisq( &pb[0] , 0 ) ;
fprintf(out_fp, "\n(chi-square = %f)\n",yave);

}

/*********************************************************************/

void OutPutDev(double *stddev, double *average)
{
 int  i , iflt , ityp ;
 double temp;

 fprintf(out_fp, "\nStandard Deviations of fit parameters\n");
 i=0;
 for(i=0;i<ndim;i++)	
    {	
     iflt = lookup[i][0] ;
     ityp = lookup[i][1] ;

     switch(ityp)
           {
            case 1:
               temp =  stddev[i] * typical_dist ;
               fprintf(out_fp, "error longitude = %e\n", temp);
               break ;

            case 2:
               temp =  stddev[i] * typical_dist ;
               fprintf(out_fp, "error latitude = %e\n", temp);
               break ;

            case 3:
               temp =  stddev[i] * (360.0/pi)/(1+average[i]*average[i]) ;
               fprintf(out_fp, "error strike = %e\n", temp);
               break ;

            case 4:
               temp =  stddev[i] * (360.0/pi)/(1+average[i]*average[i]);
               fprintf(out_fp, "error dip = %e\n", temp);
               break ;

            case 5:
               temp =  exp(average[i]) * typical_dist * stddev[i] ;
               fprintf(out_fp, "error depth = %e\n", temp);
               break ;

            case 6:
               temp =  exp(average[i]) * typical_dist * stddev[i] ;
               fprintf(out_fp, "error width = %e\n", temp);
               break ;

            case 7:
               temp =  exp(average[i]) * typical_dist * stddev[i] ;
               fprintf(out_fp, "error length = %e\n", temp);
               break ;

            case 8:
               temp =  stddev[i] * typical_slip ;
               fprintf(out_fp, "error strike-slip = %e\n", temp);
               break ;

            case 9:
               temp =  stddev[i] * typical_slip ;
               fprintf(out_fp, "error dip-slip = %e\n", temp);
               break ;
           }
    }
}

/*********************************************************************/


void OutPutCor2(double *correlation)
{
 int i, j;

 fprintf(out_fp, "\nCorrelation matrix:\n");
 for (i=0;i<ndim;i++)
	{	
	 for (j=0;j<=i;j++)
		fprintf(out_fp, "%23.15e     ", correlation[IX(j,i)]);
	 fprintf(out_fp, "\n");
	}
}

/*********************************************************************/


void OutPutCov(double *covariance)
{
 int i, j;

 fprintf(out_fp, "\nCovariance matrix:\n");
 for (i=0;i<ndim;i++)
	{	
	 for (j=0;j<=i;j++)
		fprintf(out_fp, "%23.15e     ", covariance[IX(j,i)]);
	 fprintf(out_fp, "\n");
	}
}


/*********************************************************************/
double gasdev(double sigma, long *idum)
{

  static int iset=0;
  static double gset;
  double fac, rsq, v1, v2;

  if (iset==0) {
    do {
      v1 = 2.0*ran1(idum)-1.0;
      v2 = 2.0*ran1(idum)-1.0;
      rsq = v1*v1+v2*v2;
    } while (rsq >=1.0 || rsq == 0.0);

    fac = sqrt (-2.0*sigma*sigma*log(rsq)/rsq);
    gset = v1*fac;
    iset = 1;
    return v2*fac;
  } else {
    iset=0;
    return gset;
  }
}
void transform(double *e,double *epr,double ca, double sa)
{
   int    n;
   double exy;

   for (n=0; n < 12; n += 4)
      {
      exy = 0.5*(e[n+1]+e[n+2]);
      epr[n] = e[n]*ca*ca + e[n+3]*sa*sa + 2.0*exy*sa*ca;
      epr[n+1] = -(e[n]-e[n+3])*sa*ca + exy*(ca*ca-sa*sa) -
                                       0.5*(e[n+2]-e[n+1]);
      epr[n+2] = 2.0*(e[n+3]-e[n])*sa*ca + 2.0*exy*(ca*ca-sa*sa)-epr[n+1];
      epr[n+3] = e[n]*sa*sa + e[n+3]*ca*ca - (e[n+1]+e[n+2])*sa*ca;
      }
}

/*********************************************************************/
void triangle_matrix_copy(double *a, double *b, long n)
{
   double *aend = a + (n*(n+1))/2;
   for( ; a < aend; *b++ = *a++);
}

   
