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
  
  File: square_upper_triangle_matrix.c

    Library:  Mathematics
    
    Include:  "math_tools_for_matrix_algebra.h"
    
    Abstract: This file contains a function that squares an upper triangular matrix.
      
    History:  - Date -      - Author -        - Comment -
   
              CURRENT_DATE  L. Sparks         Initial version.
                 
    RCS info: $Log$
                 
  ***********************************************************************************
  ***********************************************************************************  
*/


#include "math_tools_for_matrix_algebra.h"




/*
  ***********************************************************************************
  
  Function: square_upper_triangle_matrix
  
    Description: This function calculates the square of a given upper triangular 
                 matrix, where the square is defined as the product of the matrix 
                 and its transpose.  Since the square is a symmetric matrix, only the 
                 upper diagonal elements are calculated.  
    
    Return:      void
                
  ***********************************************************************************
*/

#define C_FUNCTION "square_upper_triangle_matrix"

void square_upper_triangle_matrix
(
  const Real *a,                  /* Pointer to the first element in the upper
                                     triangular matrix to be squared.  This matrix
                                     is stored by column, with the vanishing,
                                     subdiagonal elements omitted.
                                  */
  Real *b,                        /* Pointer, on exit, to the first element in the 
                                     calculated square of "a" (also stored by column,
                                     with the subdiagonal elements omitted).  The
                                     memory occupied by this matrix must be allocated
                                     prior to entry.  If, on entry, "a" and "b" 
                                     contain the same address, the calculated matrix 
                                     overwrites the given matrix. 
                                  */
  Int n                           /* Dimension of each matrix.
                                  */
)
         
{
  
  const Real *end_of_diagonal_elements = a + ((n * (n + 1)) / 2) + n,
             *ptr_diagonal_element     = a,
             *ptr_matrix_row_element,
             *ptr_matrix_element,
             *ptr_transpose_diagonal_element,
             *ptr_transpose_element;
  
  Real sum;
      
  Int diagonal_stride = 1,
      stride_1,
      stride_2; 
      
  
  while (ptr_diagonal_element != end_of_diagonal_elements)
  {
    ptr_matrix_row_element         = ptr_diagonal_element;
    ptr_transpose_diagonal_element = ptr_diagonal_element;
    stride_1                       = diagonal_stride; 
         
    while (ptr_transpose_diagonal_element != end_of_diagonal_elements)
    {
      ptr_matrix_element    = ptr_matrix_row_element; 
      ptr_transpose_element = ptr_transpose_diagonal_element;
      stride_2              = stride_1;
      sum                   = 0.;

      while (end_of_diagonal_elements - ptr_matrix_element > n)
      {
        sum += *ptr_matrix_element * *ptr_transpose_element;
        
        ptr_matrix_element    += stride_2;
        ptr_transpose_element += stride_2++;
      }
      
      *(b + (ptr_matrix_row_element - a)) = sum;
      
      ptr_matrix_row_element         += stride_1,
      ptr_transpose_diagonal_element += ++stride_1;
         
    } /* End of "ptr_transpose_diagonal_element" while loop.
      */
    
    ptr_diagonal_element += ++diagonal_stride;
    
  } /* End of "ptr_diagonal_element" while loop.
    */
   
  return;   
       
}

#undef C_FUNCTION
