<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="tag" scope="session" class="fr.paris.lutece.plugins.tagcloud.web.TagCloudJspBean" />

<%
    tag.init( request, tag.RIGHT_MANAGE_TAGCLOUD );
    response.sendRedirect( tag.doRemoveTag( request ) );
%>