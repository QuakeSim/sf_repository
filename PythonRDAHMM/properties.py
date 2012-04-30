#!/usr/bin/python
#==========================================================================
# Definitions of all global variables such as paths and commands used by
# data processing scripts. Imported and invoked internally. 
#
#===========================================================================

def properties(key):
    V={}
    V['download_path']="/home/yuma/RDAHMM/Download/"  
    V['script_path']="/home/yuma/PythonRDAHMM/"
    V['data_path']="/home/yuma/RDAHMM/Data/"
    V['temp_path']="/home/yuma/RDAHMM/TEMP/"
    return V[key]
