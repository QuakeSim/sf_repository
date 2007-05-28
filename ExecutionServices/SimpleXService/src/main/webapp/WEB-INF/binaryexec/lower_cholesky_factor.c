/*
  ***********************************************************************************
  ***********************************************************************************
  
                                    Jay's own code

  ***********************************************************************************
  ***********************************************************************************
  
  File: lower_cholesky_factor.c

    Library:  Mathematics
    
    Include:  "math_tools_for_matrix_algebra.h"
    
    Abstract: This file is responsible for calculating the square root of a given
              symmetric matrix.
      
    History:  - Date -      - Author -        - Comment -
   
              CURRENT_DATE  J. Parker          Initial version.
                 
    RCS info: $Log$
                 
  ***********************************************************************************
  ***********************************************************************************  
*/


#include <math.h>
#include <stdlib.h>

#include "math_tools_for_matrix_algebra.h"

#define IX(i,j) ((j*(j+1))/2 + i)
#define DIAG(i) (IX(i,i))



  
/*
  ***********************************************************************************
  
  Function: lower_cholesky_factor
  
    Description: This function calculates the square root of a given positive
                 definite symmetric matrix, where the square root is defined to be 
                 the lower triangular matrix such that the symmetric matrix is equal 
                 to the product of this matrix times its transpose (i.e., the Lower
                 Triangular Cholesky Factorization). This function is based on an 
                 algorithm appearing in Bierman, "Factorization Methods for Discrete
                 Sequential Estimation", 1977, p. 55.  
    
    Return:      Integer:
                   == -1 -> the square root matrix has been calculated;
                   >=  0 -> the square root matrix cannot be calculated because the
                            the given matrix is not a positive definite matrix; this
                            integer identifies the column in which the algorithm 
                            failed. 
                
  ***********************************************************************************
*/

#define C_FUNCTION "lower_cholesky_factor"

Int lower_cholesky_factor
(
  Real *symmetric_matrix,        /* Pointer, on entry, to the first element in the 
                                    symmetric matrix to be factored.  This matrix 
                                    is stored by column, with the symmetric, 
                                    subdiagonal elements omitted. This matrix is 
                                    destroyed by the calculation.
                                 */
  Real *root_matrix,             /* Pointer, on exit, to the first element in the 
                                    calculated square root of the "symmetric_matrix" 
                                    (also, an upper triangular matrix stored by 
                                    column, with the vanishing, subdiagonal elements
                                    omitted).  The memory occupied by this matrix 
                                    must be allocated prior to entry.  If, on entry,
                                    "symmetric_matrix" and "root_matrix" contain the
                                    same address, the calculated matrix overwrites 
                                    the given matrix. 
                                 */
  Int number_rows	         /* Number of rows in each matrix.
                                 */
)

{

  Int column_1, 
      column_2, 
      row_1,
      row_2;
      
  Real alpha, 
       beta; 

  for (row_1 = 0; row_1 < number_rows-1; row_1++)
  {
    if (symmetric_matrix[DIAG(row_1)] <= 0.)
    {  
      /* 
          Factorization has failed at this row_1
      */
     
      return row_1;  
    }
    
    root_matrix[DIAG(row_1)] = sqrt(symmetric_matrix[DIAG(row_1)]);
    alpha                        = 1.0 / root_matrix[DIAG(row_1)];

    for (column_1 = number_rows-1; column_1 > row_1; column_1--)
    {
     /* sparks upper form, modified */
      beta 
           = root_matrix[IX(row_1,column_1)] 
           = symmetric_matrix[IX(row_1,column_1)]*alpha;
      
      row_2 = column_1;
      for (column_2 = column_1; column_2 < number_rows; column_2++)
      {
         /*printf(" col1 %d col2 %d ix1 %d ix2 %d \n",column_1,column_2,IX(column_1,column_2),IX(row_1,column_2)); */
         
         symmetric_matrix[IX(row_2,column_2)] -= root_matrix[IX(row_1,column_2)]*beta;
      }
      
    } /* End of "column_1" for loop.
      */
      
  }  /* End of "row_1" for loop.
     */

  row_1 = number_rows - 1;
  
  if (symmetric_matrix[DIAG(row_1)] <= 0.) return row_1; 

  root_matrix[DIAG(row_1)] = sqrt(symmetric_matrix[DIAG(row_1)]);

  return -1;
  
}

#undef C_FUNCTION
