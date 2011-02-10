<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="tag" scope="session" class="fr.paris.lutece.plugins.tagcloud.web.TagCloudJspBean" />

<% tag.init( request, tag.RIGHT_MANAGE_TAGCLOUD ); %>
<%= tag.getModifyTag ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>