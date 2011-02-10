<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="tagCloudPortlet" scope="session" class="fr.paris.lutece.plugins.tagcloud.web.portlet.TagCloudPortletJspBean" />

<%
    tagCloudPortlet.init( request, tagCloudPortlet.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect( tagCloudPortlet.doModify( request ) );
%>
