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
  
  File: math_tools_for_matrix_algebra.h

    Library:  Mathematics
    
    Abstract: This file contains definitions and declarations that establish tools
              for performing matrix algebra.
      
    History:  - Date -      - Author -        - Comment -
   
              CURRENT_DATE  L. Sparks         Initial version.
                 
    RCS info: $Log$
                 
  ***********************************************************************************
  ***********************************************************************************  
*/


/*
    Header dependencies:
*/

#ifndef ENVIRONMENT
#include "environment.h"
#endif



/*
    Macro definitions:
*/

#define MATH_TOOLS_FOR_MATRIX_ALGEBRA 



/*
    Function declarations:
*/
Int lower_cholesky_factor
(
  Real *symmetric_matrix,
  Real *root_matrix, 
  Int number_rows 
);

void inverse_check
(
  const Real *a,                         
  const Real *b,                         
  Int n                            
);

Int invert_upper_triangle_matrix
(
  const Real *a,                         
  Real *b,                         
  Int n                            
);
         

void square_upper_triangle_matrix
(
  const Real *a,                        
  Real *b,                        
  Int n                           
);
         

Int factor_symmetric_matrix
(
  Real *symmetric_matrix,  
  Real *root_matrix,             
  Int number_columns		     
);


void define_unit_diagonal_matrix
( 
  Real *matrix, 
  Int number_columns
);

