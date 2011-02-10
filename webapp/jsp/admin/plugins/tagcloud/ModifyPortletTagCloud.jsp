<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="tagCloudPortlet" scope="session" class="fr.paris.lutece.plugins.tagcloud.web.portlet.TagCloudPortletJspBean" />

<% tagCloudPortlet.init( request, tagCloudPortlet.RIGHT_MANAGE_ADMIN_SITE); %>
<%= tagCloudPortlet.getModify( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
