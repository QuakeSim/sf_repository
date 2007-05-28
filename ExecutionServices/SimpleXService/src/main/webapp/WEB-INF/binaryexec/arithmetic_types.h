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
  
  File: arithmetic_types.h

    Library:  Environs
    
    Abstract: This file defines a set of arithmetic types (and related macros) 
              that can be used to specify the precision of real and integral 
              variables.  These definitions are currently machine-independent but 
              may become machine-dependent when source code that employs this file
              is ported to new environments. 
      
    History:  - Date -      - Author -        - Comment -
   
              CURRENT_DATE  L. Sparks         Initial version.
                 
    RCS info: $Log$

  ***********************************************************************************
  ***********************************************************************************  
*/


/*
    Header dependencies:
*/

#include <float.h>

#ifndef COMPILATION
#include "compilation.h"
#endif


HEADER_COMPILATION(arithmetic_types, "$Header$")


/*
    Definition of macro used to verify file inclusion:
*/

#define ARITHMETIC_TYPES 



/* 
    Definition of arithmetic types:
*/
  
typedef long int Int;                  /* Type for integers. 
                                       */     
typedef short int Integer2;            /* Type for two byte (sixteen bit) integers.
                                       */     
typedef long int Integer4;             /* Type for four byte (thirty-two bit) 
                                          integers.
                                       */     
typedef unsigned long int U_Int;       /* Type for unsigned integers.  
                                       */     
typedef short int U_Integer2;          /* Type for two byte (sixteen bit) unsigned
                                          integers.
                                       */     
typedef unsigned long int U_Integer4;  /* Type for four byte (thirty-two bit) 
                                          unsigned integers.
                                       */     
typedef double Real;                   /* Type for real floating-point numbers.
                                       */
typedef float Float4;                  /* Type for four byte (thirty-two bit) real 
                                          floating-point numbers.
                                       */
typedef double Float8;                 /* Type for eight byte (sixty-four bit) real 
                                          floating-point numbers.
                                       */



/*
    Definition of field macros for reading input variables and writing output 
    variables of the various arithmetic types defined above (to write an "Int", 
    for example, one can use the format 
                    
                    "%" XD 
                    
    which concatenates to form "%ld"):
*/
  
#define XD  "ld"                       /* Conversion for reading and writing       
                                          integer numbers of type "Int" or 
                                          type "U_Int".
                                       */
#define X2D "hd"                       /* Conversion for reading and writing       
                                          integer numbers of type "Integer2"
                                          or type "U_Integer2".
                                       */
#define X4D "ld"                       /* Conversion for reading and writing       
                                          integer numbers of type "Integer4"
                                          or type "U_Integer4".
                                       */
#define XE  "le"                       /* Conversion for reading and writing real  
                                          numbers of type "Real" in exponential 
                                          notation.                     
                                       */
#define X4E "e"                        /* Conversion for reading and writing real  
                                          numbers of type "Float4" in exponential 
                                          notation.                     
                                       */
#define X8E "le"                       /* Conversion for reading and writing real  
                                          numbers of type "Float8" in exponential 
                                          notation.                     
                                       */
#define XF  "lf"                       /* Conversion for reading and writing real  
                                          numbers of type "Real" in decimal 
                                          notation.                         
                                       */
#define X4F "f"                        /* Conversion for reading and writing real  
                                          numbers of type "Float4" in decimal 
                                          notation.                         
                                       */
#define X8F "lf"                       /* Conversion for reading and writing real  
                                          numbers of type "Float8" in decimal 
                                          notation.                         
                                       */
#define XG  "lg"                       /* Conversion for reading and writing real  
                                          numbers of type "Real" in exponential 
                                          or decimal notation, whichever is
                                          smaller.                
                                       */
#define X4G "g"                        /* Conversion for reading and writing real  
                                          numbers of type "Float4" in exponential 
                                          or decimal notation, whichever is
                                          smaller.                     
                                       */
#define X8G "lg"                       /* Conversion for reading and writing real  
                                          numbers of type "Float8" in exponential 
                                          or decimal notation, whichever is
                                          smaller.                 
                                       */



/*
      Definition of constants (the first set should be coordinated with the 
      definition of "Real"):
*/
  
#define MAX_NUMBER_DIGITS              DBL_DIG
#define BIG_NUMBER                     DBL_MAX
#define SMALL_NUMBER                   DBL_MIN 
#define TOLERANCE                      (100. * DBL_EPSILON)


#define MAX_FLOAT8_DIGITS              DBL_DIG
#define BIG_FLOAT8                     DBL_MAX
#define SMALL_FLOAT8                   DBL_MIN 
#define TOLERANCE8                     (100. * DBL_EPSILON)


#define MAX_FLOAT4_DIGITS              FLT_DIG
#define BIG_FLOAT4                     FLT_MAX
#define SMALL_FLOAT4                   FLT_MIN 
#define TOLERANCE4                     (100. * FLT_EPSILON)
