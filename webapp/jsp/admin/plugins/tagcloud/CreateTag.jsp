<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="tag" scope="session" class="fr.paris.lutece.plugins.tagcloud.web.TagCloudJspBean" />

<% tag.init( request, tag.RIGHT_MANAGE_TAGCLOUD ); %>
<%= tag.getCreateTag ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>