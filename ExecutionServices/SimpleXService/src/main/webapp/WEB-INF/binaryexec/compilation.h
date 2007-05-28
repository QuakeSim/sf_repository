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
  
  File: compilation.h

    Library:  Environs
    
    Abstract: This file contains definitions used to track code compilation.
      
    History:  - Date -      - Author -        - Comment -
   
              CURRENT_DATE  L. Sparks         Initial version.
                 
    RCS info: $Log$

  ***********************************************************************************
  ***********************************************************************************  
*/


/*
    Definition of macro used to verify file inclusion:
*/

#define COMPILATION 



/*
    Definition of the structure type specifying information concerning the
    compilation of a source code file:
*/
  
typedef struct
{
  const char *file,                    /* Name of a source file.
                                       */
             *date,                    /* Date of its compilation.
                                       */
             *time,                    /* Time of its compilation.
                                       */
             *rcs_id;                  /* String used to identify the source file 
                                          when working under the Revision Control
                                          System (RCS).
                                       */
} Compilation_information;



/*
    Definition of macros used to store and retrieve information concerning the 
    compilation of source code:
*/
  
#define SOURCE_COMPILATION(SOURCE_FILE_NAME, C_INFO, ID)                            \
        static Compilation_information C_INFO = {__FILE__, __DATE__, __TIME__, ID}; \
        const Compilation_information *X ## SOURCE_FILE_NAME ## _(void)             \
        { return &C_INFO; }

#define DECLARE_SOURCE_COMPILATION(SOURCE_FILE_NAME)                                \
        const Compilation_information *X ## SOURCE_FILE_NAME ## _(void); 

#define GET_SOURCE_COMPILATION(SOURCE_FILE_NAME)                                    \
        X ## SOURCE_FILE_NAME ## _() 

#define HEADER_COMPILATION(HEADER_FILE_NAME, ID)                                    \
        static char HEADER_FILE_NAME ## _h[] = ID;


HEADER_COMPILATION(compilation, "$Header$")
