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
    V['model_path']="/home/yuma/RDAHMM/Model/"
    V['eval_path']="/home/yuma/RDAHMM/Eval/"
    V['train_epoch']="2011-12-31"
    return V[key]
