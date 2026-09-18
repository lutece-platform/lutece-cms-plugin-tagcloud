<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<%@page import="fr.paris.lutece.plugins.tagcloud.web.portlet.TagCloudPortletJspBean"%>

${ tagCloudPortletJspBean.init( pageContext.request, TagCloudPortletJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ tagCloudPortletJspBean.getCreate( pageContext.request ) }

<%@ include file="../../AdminFooter.jsp" %>
