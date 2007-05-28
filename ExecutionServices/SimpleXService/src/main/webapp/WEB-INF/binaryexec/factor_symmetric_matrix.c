/*
  ***********************************************************************************
  ***********************************************************************************
  
     Sequential Evaluation Algorithm for Simultaneous and Concurrent Retrieval of    
                      Atmospheric Parameter Estimates (SEASCRAPE)                  
   
                               Jet Propulsion Laboratory 
                               
                Copyright (c) 1995, California Institute of Technology.     
            U. S. Sponsorship under NASA Contract NAS7-1270 is acknowledged.
                               
  ***********************************************************************************
  ***********************************************************************************
  
  File: factor_symmetric_matrix.c

    Library:  Mathematics
    
    Include:  "math_tools_for_matrix_algebra.h"
    
    Abstract: This file is responsible for calculating the square root of a given
              symmetric matrix.
      
    History:  - Date -      - Author -        - Comment -
   
              CURRENT_DATE  S. Nandi          Initial version.
                 
    RCS info: $Log$
                 
  ***********************************************************************************
  ***********************************************************************************  
*/


#include <math.h>
#include <stdlib.h>

#include "math_tools_for_matrix_algebra.h"



  
/*
  ***********************************************************************************
  
  Function: factor_symmetric_matrix
  
    Description: This function calculates the square root of a given positive
                 definite symmetric matrix, where the square root is defined to be 
                 the upper triangular matrix such that the symmetric matrix is equal 
                 to the product of this matrix times its transpose (i.e., the Upper 
                 Triangular Cholesky Factorization). This function is based on an 
                 algorithm appearing in Bierman, "Factorization Methods for Discrete
                 Sequential Estimation", 1977, p. 53.  
    
    Return:      Integer:
                   == -1 -> the square root matrix has been calculated;
                   >=  0 -> the square root matrix cannot be calculated because the
                            the given matrix is not a positive definite matrix; this
                            integer identifies the column in which the algorithm 
                            failed. 
                
  ***********************************************************************************
*/

#define C_FUNCTION "factor_symmetric_matrix"

Int factor_symmetric_matrix
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
  Int number_columns		     /* Number of columns in each matrix.
                                 */
)

{

  Int column_index = number_columns - 1,
      diagonal_offset;
      
  Real alpha, 
       beta, 
       *ptr_symmetric_element_1,
       *ptr_symmetric_element_2,
       *ptr_root_element_1, 
       *ptr_root_element_2, 
       *end_of_column;

  for (; column_index > 0; column_index--)
  {
    diagonal_offset         = (column_index * (column_index + 3)) / 2;
    ptr_symmetric_element_2 = symmetric_matrix;

    if (symmetric_matrix[diagonal_offset] <= 0.)
    {  
      /* 
          Factorization has failed at this column.
      */
     
      return column_index;  
    }
    
    root_matrix[diagonal_offset] = sqrt(symmetric_matrix[diagonal_offset]);
    alpha                        = 1.0 / root_matrix[diagonal_offset];

    end_of_column           = root_matrix      + diagonal_offset;
    ptr_root_element_1      = root_matrix      + diagonal_offset - column_index;
    ptr_symmetric_element_1 = symmetric_matrix + diagonal_offset - column_index;
    
    for (; ptr_root_element_1 < end_of_column; 
           ptr_root_element_1++, ptr_symmetric_element_1++)
    {
      beta = *ptr_root_element_1 = *ptr_symmetric_element_1 * alpha;
      
      ptr_root_element_2 = root_matrix + diagonal_offset - column_index;
      
      for (; ptr_root_element_2 <= ptr_root_element_1; 
             ptr_root_element_2++, ptr_symmetric_element_2++)
      { 
        *ptr_symmetric_element_2 -= beta * *ptr_root_element_2;
      }
      
    } /* End of "ptr_root_element_1" for loop.
      */
      
  }  /* End of "column_index" for loop.
     */
  
  if (*symmetric_matrix <= 0.) return 0; 

  *root_matrix = sqrt(*symmetric_matrix);

  return -1;
  
}

#undef C_FUNCTION
