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
  
  File: invert_upper_triangle_matrix.c

    Library:  Mathematics
    
    Include:  "math_tools_for_matrix_algebra.h"
    
    Abstract: This file contains a function that inverts an upper triangular matrix.
      
    History:  - Date -      - Author -        - Comment -
   
              CURRENT_DATE  L. Sparks         Initial version.
                 
    RCS info: $Log$
                 
  ***********************************************************************************
  ***********************************************************************************  
*/


#include <math.h>

#include "math_tools_for_matrix_algebra.h"




/*
  ***********************************************************************************
  
  Function: invert_upper_triangle_matrix
  
    Description: This function inverts a given upper triangular matrix.  
    
                 This function is based upon an algorithm presented by Gerald J. 
                 Bierman on page 65 of Factorization Methods for Discrete Sequential 
                 Estimation (Academic Press: Orlando, Florida, 1977).  
    
    Return:      0           -> the matrix has been successfully inverted;
                 integer > 0 -> the number of diagonal elements that vanish (i.e., 
                                the matrix is singular and, therefore, cannot be 
                                inverted).
                
  ***********************************************************************************
*/

#define C_FUNCTION "invert_upper_triangle_matrix"

Int invert_upper_triangle_matrix
(
  const Real *a,                 /* Pointer to the first element in the upper
                                    triangular matrix to be inverted.  This matrix
                                    is stored by column, with the vanishing, 
                                    subdiagonal elements omitted.
                                 */
  Real *b,                       /* Pointer, on exit, to the first element in the 
                                    calculated inverse of "a" (also, an upper 
                                    triangular matrix stored by column, with the 
                                    vanishing, subdiagonal elements omitted).  The
                                    memory occupied by this matrix must be allocated 
                                    prior to entry.  If, on entry, "a" and "b" 
                                    contain the same address, the calculated matrix 
                                    overwrites the given matrix. 
                                 */
  Int n                          /* Dimension of each matrix.
                                 */
)
         
{
  
  const Real *end_of_diagonal_elements        = a + (n * ((n + 1)) / 2) + n,
             *ptr_diagonal_element_of_a       = a,
             *ptr_prior_diagonal_element_of_a = a - 1,
             *ptr_element_of_a,
             *ptr_column_element_of_a;
      
  Real *ptr_diagonal_element_of_b = b,
       *ptr_prior_diagonal_element_of_b,
       *ptr_element_of_b,
       sum;

  Int count_zero_diagonal_elements = 0,
      diagonal_stride              = 1,
      column_stride; 
      
  
  while (ptr_diagonal_element_of_a != end_of_diagonal_elements)
  {
    if (fabs(*ptr_diagonal_element_of_a) < TOLERANCE)
    {
      count_zero_diagonal_elements++;
    }
    else
    {
      *ptr_diagonal_element_of_b = 1. / *ptr_diagonal_element_of_a;

      for (ptr_prior_diagonal_element_of_b = b - 1,
           ptr_column_element_of_a         = ptr_prior_diagonal_element_of_a + 1;
           ptr_column_element_of_a         < ptr_diagonal_element_of_a; 
           ptr_column_element_of_a++)
      {
        column_stride = ptr_column_element_of_a - ptr_prior_diagonal_element_of_a;
      
        ptr_prior_diagonal_element_of_b += column_stride;
      
        sum = 0.;
      
        for (ptr_element_of_b = ptr_prior_diagonal_element_of_b,
             ptr_element_of_a = ptr_column_element_of_a; 
             ptr_element_of_a < ptr_diagonal_element_of_a; 
             ptr_element_of_a++, ptr_element_of_b += column_stride++)
        {
          sum += *ptr_element_of_b * *ptr_element_of_a;
        }
      
        *ptr_element_of_b = -sum * *ptr_diagonal_element_of_b;
      
      } /* End of "ptr_column_element_of_a" for loop.
        */
   
    } /* End of if/else statement.
      */
        
    ptr_prior_diagonal_element_of_a = ptr_diagonal_element_of_a;
    ptr_diagonal_element_of_a      += ++diagonal_stride;
    ptr_diagonal_element_of_b      += diagonal_stride;
    
  } /* End of while loop.
    */
   
  return count_zero_diagonal_elements;   
       
}

#undef C_FUNCTION
