<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.tagcloud.web.portlet.TagCloudPortletJspBean"%>

${ tagCloudPortletJspBean.init( pageContext.request, TagCloudPortletJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ pageContext.response.sendRedirect( tagCloudPortletJspBean.doModify( pageContext.request ) ) }

