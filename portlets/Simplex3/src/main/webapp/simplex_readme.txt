Getting Started with SIMPLEX v. 4.0

Greg Lyzenga  (lyzenga@hmc.edu)
Jay Parker    (Jay.W.Parker@jpl.nasa.gov)

20 Jan 1999

Welcome to the SIMPLEX fault inversion/optimization code.  This 
document is intended to accompany the postscript file 'simplex4.ps', 
which contains an annotated template for SIMPLEX input files.

The basic idea behind SIMPLEX is very straightforward.  You provide 
the program with a model, consisting of one or more rectangular 
dislocation faults in an elastic half space.  The parameters (such as 
dip, strike, depth etc.) describing each of these model faults may be 
given prescribed values, or may be allowed to vary.  Then you provide 
the program with data which is supposed to constrain the fault 
parameters.  The current version of SIMPLEX supports a number of data 
types including 3-D displacements (as measured by GPS for example), 
strain components (from a strain meter), or line-of-sight 
displacement (as obtained from SAR interferometry).  The 3-D 
displacement data can be provided either in an "absolute frame" 
relative to motionless earth distant from all faults, or relative to 
some reference site which you specify.

SIMPLEX works by iteratively searching parameter space for the 
combination of fault parameters which minimizes the chi-squared 
objective function formed from the measurement data and the fault 
model.  You can instruct SIMPLEX to carry out this search in either 
of two ways.  The simplest is using the straight downhill simplex 
method only.  If the chi-squared minimum is well-defined and unlikely 
to have local "false minima" that will trap the program in a bad 
solution, this method is preferred.  The program will automatically 
terminate its search when it determines that it is no longer making 
significant progress toward lower chi-squared; you can override this 
by specifying a maximum number of iterations to try before giving up. 
(The recommended value for this number is highly problem-dependent, 
and must be learned by experience.)

If you minimum is less well-defined or masked by subsidiary minima, 
you may want to use the "simulated annealing" option.  By setting the 
"temperature" paramter to a non-zero value you allow the search 
algorithm to sometimes take uphill steps with a probability given by 
a statistical Boltzmann distribution.  This gives it the ability to 
"hop" out of local minima.  The idea would be to start with the 
temperature high (around 1), and then turn it slowly down to zero 
between rounds of iteration, thus "annealing" the solution in the 
global "minimum energy" state.  Just what is the best rate at which 
to anneal the solution is very much an area of unresolved research, 
so you'll have to wing it if you want to try this option.

Notes on accompanying input template:
(1)  The conventions used for defining the fault parameters such as 
strike, dip, etc. are the same as those use by Andrea Donnellan's 
utility, disloc.  See the documentation postscript file 'disloc.ps' 
for these definitions.

(2)  Each parameter line contains:
     (parameter code #)  (fixed/free code #)  (starting guess or fixed value)

(3)  Each data line contains:
(meas. type #) (east location) (north location) (meas. value) (meas. uncert.)
SAR measurements all include two more numbers to specify line of sight.

(4)  A negative measurement type flags the program to use this 
location as the reference site for displacement measurements.  All 
other displacements are assumed to be relative to this site.  If no 
refernce site is given, the displacements are assumed absolute.

Notes on compiling and running:

The code is in /net/jimbo/usr2/jwp/src/simplex.

The sources are
arithmetic_types.h               inverse_check.c
compilation.h                    invert_upper_triangle_matrix.c
environment.h                    lower_cholesky_factor.c
math_tools_for_matrix_algebra.h  ran.c
ran.h                            simplexfd.c
factor_symmetric_matrix.c        square_upper_triangle_matrix.c

These can be compiled on my machine with the one-liner:
>  cc -Aa -o simplex -g simplexfd.c ran.c lower_cholesky_factor.c
>inverse_check.c  invert_upper_triangle_matrix.c
>square_upper_triangle_matrix.c -lm

The test input file is simplex.input (an 11 x 11 grid of x, y, z 
displacements); the output is in outfile. Note outfile contains the 
parameter final estimates, parameter formal errors, the correlation 
and covariance matrices,  the residuals on the observations, and the 
final chi-square. Concerning units: the angles must be degrees; all 
the other paramters can be in any internally consistent system of 
units.  For example, if dislacement is given in mm, then solved-for 
fault slip will be mm also.
if site locations are in km, fault dimensions and locations will be also.

The following is a transcript of terminal standard output that you 
should obtain when running the simplex.input example; solution time 
is about a half-minute:

>  simplex simplex.input outfile
# of faults = 1
# of free parameters = 6
# of data points = 363
temperature = 0.000000 , iterations = 1000, 'trials' = 1
Simplex filled...
chisq(0) = 0.000679
chisq(1) = 162.868152
chisq(2) = 143.948540
chisq(3) = 132.380317
chisq(4) = 65.116003
chisq(5) = 34947266.502425
chisq(6) = 25847958.535945
functions initialized...
start temp = 0.000000
count = 100, chi-squared per dof = 0.000679
count = 201, chi-squared per dof = 0.000486
count = 300, chi-squared per dof = 0.000455
count = 401, chi-squared per dof = 0.000455
Amebsa has met convergence criterion.
number of steps taken: 401


******* best-ever solution ********
best-ever chi-square (yb) = 0.000455055