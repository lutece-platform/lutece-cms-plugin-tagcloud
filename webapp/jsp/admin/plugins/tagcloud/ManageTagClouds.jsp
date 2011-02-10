<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="tagcloud" scope="session" class="fr.paris.lutece.plugins.tagcloud.web.TagCloudJspBean" />

<% tagcloud.init( request, tagcloud.RIGHT_MANAGE_TAGCLOUD ); %>
<%= tagcloud.getManageTagClouds ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>