/*
  ***********************************************************************************
  ***********************************************************************************
  
                                    Jay's own code
                               
  ***********************************************************************************
  ***********************************************************************************
  
  File: inverse_check.c

    Library:  Mathematics
    
    Include:  "math_tools_for_matrix_algebra.h"
    
    Abstract: This file contains a function that checks if two matrices are inverses.
              Note restrictions: the matrices are assumed symmetric, stored as columns
              of upper triangle (including diagonal).
      
    History:  - Date -      - Author -        - Comment -
   
              4/15/97		jayparker	no previous exists
                 
    RCS info: $Log$
                 
  ***********************************************************************************
  ***********************************************************************************  
*/


#include <math.h>

#include "math_tools_for_matrix_algebra.h"
#define IXS(i,j) ((j >= i) ? j*(j+1)/2 + i : i*(i+1)/2 + j)




/*
  ***********************************************************************************
  
  Function: inverse_check
  
    Description: This function checks if two triangular matrices are inversely related
    
    
    Return:      nothing, but prints a bunch out
                
  ***********************************************************************************
*/


void inverse_check
(
  const Real *a,                 /* Pointer to the first element in the left upper
                                    triangular matrix.  This matrix
                                    is stored by column, with the vanishing, 
                                    subdiagonal elements omitted.
                                 */
  const Real *b,                 /* Pointer to the first element in the right upper
                                    triangular matrix.  This matrix
                                    is stored by column, with the vanishing, 
                                    subdiagonal elements omitted.
                                 */
  Int n                          /* Dimension of each matrix.
                                 */
)
         
{
  
  Real diagsum = 0,
       offdiagsum = 0,
       sum = 0;

  Int rowa, rowb, cola, colb;

      
  for(rowa = 0; rowa < n; rowa++)
  {
    for(colb = 0; colb < n; colb++)
    {
      for(cola = 0,sum = 0.; cola < n; cola++)
      {
        /* obtain term c(rowa,colb) */
        rowb = cola;
        sum += a[IXS(rowa,cola)] * b[IXS(rowb,colb)];
      }
      /*printf("Elt %d, %d = %f \n",rowa,colb,sum);*/
      if(rowa == colb) diagsum += fabs(sum);
      if(rowa != colb) offdiagsum += fabs(sum);
    }
  }
  if( (diagsum<0.999*n) || (diagsum>1.001*n) ){
  printf("INVERSE_CHECK warning! :diagonal avg magnitude = %f   \n",diagsum/n);
  }
  if(fabs(offdiagsum)>1e-4)
  printf("INVERSE_CHECK warning!: off-diagonal total summed magnitudes = %f \n",offdiagsum);
}

