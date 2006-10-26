<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>RDAHMM Minimalist Output</title>
  </head>
 <body>
 Click the link and get the data.  Welcome to what I like to call "the
Internet".
<br>
    <b>Output Files </b>

  <f:view>        
    <h:form>
      <h:panelGrid>
       <h:outputLink value="#{simpleRdahmmClientBean.projectInput}">
       <h:outputText value="Input File"/>
       </h:outputLink>


       <h:outputLink value="#{simpleRdahmmClientBean.projectRange}">
       <h:outputText value="Range"/>
       </h:outputLink>

       <h:outputLink value="#{simpleRdahmmClientBean.projectQ}">
       <h:outputText value="Optimal State Sequence File (Q)"/>
       </h:outputLink>


       <h:outputLink value="#{simpleRdahmmClientBean.projectA}">
       <h:outputText value="Model Transition Probability (A)"/>
       </h:outputLink>


       <h:outputLink value="#{simpleRdahmmClientBean.projectB}">
       <h:outputText value="Model Output Distribution (B)"/>
       </h:outputLink>


       <h:outputLink value="#{simpleRdahmmClientBean.projectL}">
       <h:outputText value="Model Log Likelihood (L)"/>
       </h:outputLink>>


       <h:outputLink value="#{simpleRdahmmClientBean.projectPi}">
       <h:outputText value="Model Initial State Probability (PI)"/>
       </h:outputLink>

       <h:outputLink value="#{simpleRdahmmClientBean.projectMinval}">
       <h:outputText value="Minimum Value"/>
       </h:outputLink>

       <h:outputLink value="#{simpleRdahmmClientBean.projectMaxval}">
       <h:outputText value="Maximum Value"/>
       </h:outputLink>

       <h:outputLink value="#{simpleRdahmmClientBean.projectGraphX}">
       <h:outputText value="Plot of X Values"/>
       </h:outputLink>

       <h:outputLink value="#{simpleRdahmmClientBean.projectGraphY}">
       <h:outputText value="Plot of Y Values"/>
       </h:outputLink>

       <h:outputLink value="#{simpleRdahmmClientBean.projectGraphZ}">
       <h:outputText value="Plot of Z Values"/>
       </h:outputLink>

      </h:panelGrid>
    </h:form>

  </f:view>

 </body>
</html>
