<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="tagcloud" scope="session" class="fr.paris.lutece.plugins.tagcloud.web.TagCloudJspBean" />

<%
    tagcloud.init( request, tagcloud.RIGHT_MANAGE_TAGCLOUD );
    response.sendRedirect( tagcloud.doRemoveTagCloud( request ) );
%>

