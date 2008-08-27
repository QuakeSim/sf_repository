<%--
  The contents of this file are subject to the terms
  of the Common Development and Distribution License
  (the License).  You may not use this file except in
  compliance with the License.
 
  You can obtain a copy of the license at
  http://www.sun.com/cddl/cddl.html or
  at portlet-repository/CDDLv1.0.txt.
  See the License for the specific language governing
  permissions and limitations under the License.
 
  When distributing Covered Code, include this CDDL
  Header Notice in each file and include the License file
  at portlet-repository/CDDLv1.0.txt.
  If applicable, add the following below the CDDL Header,
  with the fields enclosed by brackets [] replaced by
  you own identifying information:
  "Portions Copyrighted [year] [name of copyright owner]"
 
  Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 --%>

<%@ page import="javax.portlet.PortletURL" %>
<%@ page import="javax.portlet.PortletMode" %>
<%@ page import="javax.portlet.PortletSession" %>

<%@ page import="java.io.StringWriter" %>
<%@ page import="java.io.PrintWriter" %>

<%@ page import="com.sun.portal.rssportlet.FormNames" %>

<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<!-- 
this function sets the "maximum feed age" text box either disabled or
enabled depending on the value of the "disable maximum feed age" check box
-->
<script language="javascript">
    function setMaxAge() {
    if (document.inputForm.<%=FormNames.INPUT_DISABLE_MAX_AGE%>.checked == true) {
    document.inputForm.<%=FormNames.INPUT_MAX_AGE%>.disabled = true;    
    } else {
    document.inputForm.<%=FormNames.INPUT_MAX_AGE%>.disabled = false;    
    }
    }
</script>

<portlet:defineObjects/>
<portlet:actionURL var="actionURL"/>
<fmt:setBundle basename="edit"/>

<c:catch var="exception">    
    <jsp:useBean id="alertHandler" class="com.sun.portal.rssportlet.AlertHandler" scope="session"/>           
    <jsp:useBean id="settingsBean" class="com.sun.portal.rssportlet.SettingsBean"/>       
    <jsp:useBean id="settingsHandler" class="com.sun.portal.rssportlet.SettingsHandler">
        <jsp:setProperty name="settingsHandler" property="portletRequest" value="<%=renderRequest%>"/>
        <jsp:setProperty name="settingsHandler" property="portletConfig" value="<%=portletConfig%>"/>  
    </jsp:useBean>

    <% 
        // jsp doesn't seem to like me doing this in a jsp:setProperty
        settingsHandler.setSettingsBean(settingsBean);
        // so we can get them from a portlet action
        renderRequest.getPortletSession().setAttribute("alertHandler", alertHandler, PortletSession.PORTLET_SCOPE);
    %>
    
    <!-- START title bar -->
    <table style="width: 100%">
        <tr>                
            <td style="background-color: #666699" colspan="2">
                <div style="font-weight: bold; font-size: larger; color: white; left: 8px">
                    <fmt:message key="customize_rss_feeds" />                
                </div>
            </td>
        </tr>
        <tr><td>&nbsp;</td></tr>    
    </table>
    <!-- END title bar -->
   
    <c:if test="${alertHandler.errorRendered}">
        <p>
            <div class="portlet-msg-error" style="color: red">
                <c:out value="${alertHandler.errorSummary}"/>
                <div style="font-size: smaller">
                    <c:out value="${alertHandler.errorDetail}"/>            
                </div>
            </div>
        </p>
    </c:if>

    <form name="inputForm" target="_self" method="POST" action="<%=actionURL.toString()%>">
        <table border="0" style="align: center">
            <!-- START feed checboxes -->
            <c:choose>
                <c:when test="${settingsBean.feedsSize != 0}">
                    <c:forEach var="feed" items="${settingsBean.feeds}">
                        <tr>
                            <td align="left">
                                <input class="portlet-font" type="checkbox" name="<%=FormNames.INPUT_FEEDS%>" value="<c:out value="${feed}"/>" CHECKED/>
                                <a href="<c:out value="${feed}"/>"><c:out value="${feed}"/></a>
                            </td>
                        </tr>        
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td align="left">
                            <fmt:message key="no_feeds_list" />
                        </td>
                    </tr>                    
                </c:otherwise>
            </c:choose>
            <!-- END feed checboxes -->

            <tr><td>&nbsp;</td></tr>
            <tr><td><hr style="width: 100%; text-align: center"/></td></tr>
            <tr><td>&nbsp;</td></tr>
                
            <!-- START start feed drop down list -->        
            <tr>
                <td style="align: left">
                    <table border="0">
                    <tr>
                        <td style="text-align: right; vertical-align: top">
                            <div class="portlet-form-field-label">
                                <fmt:message key="start_feed" />
                            </div>
                        </td>
                        <td style="vertical-align: top">
                            <c:choose>
                                <c:when test="${settingsBean.feedsSize > 0}">                   
                                    <select name="<%=FormNames.INPUT_START_FEED%>">
                                        <c:forEach var="feed" items="${settingsBean.feeds}">
                                            <c:choose>
                                                <c:when test="${feed == settingsBean.startFeed}">
                                                    <option value="<c:out value="${feed}"/>" selected>
                                                        <div class="portlet-font">                    
                                                            <c:out value="${feed}"/>        
                                                        </div>
                                                    </option>                           
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="<c:out value="${feed}"/>">
                                                        <div class="portlet-font">                    
                                                            <c:out value="${feed}"/>        
                                                        </div>
                                                    </option>                                                       
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="no_feeds_start" />
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </td>
            </tr>
                <!-- END start feed drop down list -->
        
                <tr><td>&nbsp;</td></tr>        
                
                <!-- START "add feed" text box + button -->                
                <tr>
                    <td style="text-align: right; vertical-align: top">
                        <div class="portlet-form-field-label">                
                            <fmt:message key="additional_rss_feed" />
                        </div>
                    </td>
                    <td style="vertical-align: top">
                        <input class="portlet-form-input-field" type="text" name="<%=FormNames.INPUT_ADD_FEED%>" value="">&nbsp;
                        <input class="portlet-form-button" name="<%=FormNames.SUBMIT_ADD%>" type="submit" value="<fmt:message key="add"/>">     
                    </td>
                </tr>
                
                <tr><td>&nbsp;</td></tr>
                
                <!-- START "maximum feed age" text box + disable -->

                <tr>
                    <td style="text-align: right; vertical-align: top">
                        <div class="portlet-form-field-label">
                            <fmt:message key="maximum_feed_age_hours" />
                        </div>
                    </td>
                    <td style="vertical-align: top">
                        <div class="portlet-form-input-field">
                            <input type="text" name="<%=FormNames.INPUT_MAX_AGE%>" value="<c:out value="${settingsBean.maxAge}"/>">
                        </div>
                    </td>
                </tr>  
                <tr>
                    <td style="text-align: right; vertical-align: top">
                        <div class="portlet-form-field-label">
                            <fmt:message key="disable" />
                        </div>
                    </td>
                    <td style="vertical-align: top">
                        <c:choose>
                            <c:when test="${settingsBean.disableMaxAge}">
                                <input type="checkbox" name="<%=FormNames.INPUT_DISABLE_MAX_AGE%>" value="" checked onclick='javascript:setMaxAge();'>                                                    
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" name="<%=FormNames.INPUT_DISABLE_MAX_AGE%>" value="" onclick='javascript:setMaxAge();'>                                                
                            </c:otherwise>
                        </c:choose>
                        <script language="javascript">setMaxAge();</script>                        
                    </td>
                </tr>
                
                <!-- END "maximum feed age" text box + disable -->
                
                <tr><td>&nbsp;</td></tr>              
                
                <!-- START "maximum entries" text box -->                
                <tr>
                    <td style="text-align: right; vertical-align: top">
                        <div class="portlet-form-field-label">
                            <fmt:message key="maximum_entries" />
                        </div>
                    </td>
                    <td style="vertical-align: top">
                        <input class="portlet-form-input-field" type="text" name="<%=FormNames.INPUT_MAX_ENTRIES%>" value="<c:out value="${settingsBean.maxEntries}"/>"/>
                    </td>
                </tr>                                
                <tr><td>&nbsp;</td></tr>
                <!-- END "maximum entries" text box -->
                
                <!-- START "new window" check box -->                
                <tr>
                    <td style="text-align: right; vertical-align: top">
                        <div class="portlet-form-field-label">
                            <fmt:message key="new_window_for_entries" />
                        </div>
                    </td>
                    <td style="vertical-align: top">
                        <c:choose>
                            <c:when test="${settingsBean.newWindow}">
                                <input type="checkbox" name="<%=FormNames.INPUT_NEWWIN%>" value="" checked/>                                                    
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" name="<%=FormNames.INPUT_NEWWIN%>" value=""/>                                                
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>        
                <!-- END "new window" check box -->
                
            </table>
            </td>
            </tr>
        
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
        
            <!-- START "finished" and "cancel" buttons -->        
            <tr><td align="center">
                <input class="portlet-form-button" name="<%=FormNames.SUBMIT_EDIT%>" type="submit" value="<fmt:message key="finished"/>"/>
                <input class="portlet-form-button" name="<%=FormNames.SUBMIT_CANCEL%>" type="submit" value="<fmt:message key="cancel"/>"/>
            </td>
            </tr>
            <!-- END "finished" and "cancel" buttons -->                
        </table>
    </form>
</c:catch>

<c:if test="${exception != null}">
    <% 
        Throwable t = (Throwable)pageContext.getAttribute("exception");
        StringWriter st = new StringWriter();
        PrintWriter pw = new PrintWriter(st);
        t.printStackTrace(pw);
        portletConfig.getPortletContext().log("exception occured in edit.jsp", t);
    %>
    <div class="portlet-msg-error" style="color: red">
        <fmt:message key="exception"/>
        <div style="font-size: smaller">
            <pre><%= t.getMessage() %></pre>            
        </div>
    </div>
   <!--
   <%= st %>
   -->   
</c:if>
