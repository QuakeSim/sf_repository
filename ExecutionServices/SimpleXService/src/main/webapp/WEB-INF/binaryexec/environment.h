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
  
  File: environment.h

    Library:  Environs
    
    Abstract: This file contains definitions that establish the computational 
              environment. 
      
    History:  - Date -      - Author -        - Comment -
   
              CURRENT_DATE  L. Sparks         Initial version.
                 
    RCS info: $Log$

  ***********************************************************************************
  ***********************************************************************************  
*/


/*
    Header dependencies:
*/

#ifndef COMPILATION
#include "compilation.h"
#endif

#ifndef ARITHMETIC_TYPES
#include "arithmetic_types.h"
#endif


HEADER_COMPILATION(environment, "$Header$")


/*
    Definition of macro used to verify file inclusion:
*/

#define ENVIRONMENT 


/*
    Definition of macros that determine the maximum and minimum of a pair of numbers:
*/
  
#define MIN(X, Y)                      ((X) < (Y) ? (X) : (Y))
#define MAX(X, Y)                      ((X) > (Y) ? (X) : (Y))



/*
    Definition of macros specifying logical flags:
*/
  
#define TRUE                           1
#define FALSE                          0



/*
    Definition of macros specifying return flags:
*/
  
#define SUCCESS                        1
#define FAILURE                        0
#define ERROR                         -1



/*
    Definition of NULL macro:
*/
  
#ifndef NULL
  #define NULL                         0
#endif
