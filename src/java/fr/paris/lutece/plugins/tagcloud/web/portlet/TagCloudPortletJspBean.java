/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.tagcloud.web.portlet;

import fr.paris.lutece.plugins.tagcloud.business.TagHome;
import fr.paris.lutece.plugins.tagcloud.business.portlet.TagCloudPortlet;
import fr.paris.lutece.plugins.tagcloud.business.portlet.TagCloudPortletHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage tagcloud Portlet features
 */
@RequestScoped
@Named
public class TagCloudPortletJspBean extends PortletJspBean
{
    private static final long serialVersionUID = 1L;

    private static final String ACTION_CREATE_PORTLET = "tagcloud.createPortlet";
    private static final String ACTION_MODIFY_PORTLET = "tagcloud.modifyPortlet";
    private static final String MESSAGE_INVALID_TOKEN = "tagcloud.message.invalidToken";
    ///////////////////////////////////////////////////////////////////////////////////
    // Constants

    /**
     * The rights required to use TagCloudPortletJspBean
     */
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";

    ////////////////////////////////
    private static final String PARAMETER_PAGE_ID = "page_id";
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_PORTLET_TYPE_ID = "portlet_type_id";
    private static final String PARAMETER_TAGCLOUD_ID = "tagcloud_id";
    private static final String COMBO_CLOUD_LIST = "@combo_clouds@";
    private static final String MARK_TAGCLOUD_LIST = "tagcloud_list";
    private static final String MARK_TAGCLOUD_ID = "default_cloud_id";
    private static final String PROPERTY_PLUGIN_NAME = "tagcloud";

    //////////////////////////////////////////////////////////////////////////////////
    //Templates
    private static final String TEMPLATE_COMBO_CLOUDS = "admin/plugins/tagcloud/portlet/combo_tagcloud.html";

    /**
     * Returns the properties prefix used for tagcloud portlet and defined in lutece.properties file
     *
     * @return the value of the property prefix
     */
    public String getPropertiesPrefix(  )
    {
        return "portlet.tagcloud";
    }

    /**
     * Returns the TagCloud Portlet form of creation
     *
     * @param request The Http rquest
     * @return the html code of the tagcloud portlet form
     */
    public String getCreate( HttpServletRequest request )
    {
        // Use the id in the request to load the portlet
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        String strPortletTypeId = request.getParameter( PARAMETER_PORTLET_TYPE_ID );

        if ( !isNumeric( strPageId ) || ( strPortletTypeId == null ) || strPortletTypeId.trim( ).isEmpty( ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        HashMap<String, Object> model = new HashMap<>( );
        model.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, ACTION_CREATE_PORTLET ) );
        HtmlTemplate template = getCreateTemplate( strPageId, strPortletTypeId, model );

        //List of clouds present
        Plugin plugin = PluginService.getPlugin( PROPERTY_PLUGIN_NAME );
        ReferenceList listClouds = TagHome.getAllTagClouds( plugin );
        String strHtmlCombo = getCloudsCombo( listClouds, "" );
        template.substitute( COMBO_CLOUD_LIST, strHtmlCombo );

        return template.getHtml(  );
    }

    /**
     * Returns the TagCloud Portlet form for update
     * @param request The Http request
     * @return the html code of the tagcloud portlet form
     */
    public String getModify( HttpServletRequest request )
    {
        TagCloudPortlet portlet = getPortlet( request.getParameter( PARAMETER_PORTLET_ID ) );

        if ( portlet == null )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        HashMap<String, Object> model = new HashMap<>( );
        model.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, ACTION_MODIFY_PORTLET ) );
        HtmlTemplate template = getModifyTemplate( portlet, model );

        // Get the plugin for the portlet
        Plugin plugin = PluginService.getPlugin( portlet.getPluginName(  ) );

        // fills the template with specific values
        ReferenceList listClouds = TagHome.getAllTagClouds( plugin );
        String strHtmlCombo = getCloudsCombo( listClouds, "" + portlet.getIdCloud(  ) );
        template.substitute( COMBO_CLOUD_LIST, strHtmlCombo );

        return template.getHtml(  );
    }

    /**
     * Treats the creation form of a new tagcloud portlet
     * @param request The Http request
     * @return The jsp URL which displays the view of the created tagcloud portlet
     */
    public String doCreate( HttpServletRequest request )
    {
        if ( !getSecurityTokenService( ).validate( request, ACTION_CREATE_PORTLET ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_TOKEN, AdminMessage.TYPE_STOP );
        }

        TagCloudPortlet portlet = new TagCloudPortlet(  );

        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        String strCloudId = request.getParameter( PARAMETER_TAGCLOUD_ID );

        if ( !isNumeric( strIdPage ) || !isNumeric( strCloudId ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nIdPage = Integer.parseInt( strIdPage );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        portlet.setPageId( nIdPage );

        //gets the specific parameters
        portlet.setIdCloud( Integer.parseInt( strCloudId ) );

        //Portlet creation (the cloud association is stored by the DAO insert flow)
        TagCloudPortletHome.getInstance(  ).create( portlet );

        //Displays the page with the new Portlet
        return getPageUrl( nIdPage );
    }

    /**
     * Treats the update form of the tagcloud portlet whose identifier is in the http request
     *
     * @param request The Http request
     * @return The jsp URL which displays the view of the updated portlet
     */
    public String doModify( HttpServletRequest request )
    {
        if ( !getSecurityTokenService( ).validate( request, ACTION_MODIFY_PORTLET ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_TOKEN, AdminMessage.TYPE_STOP );
        }

        // fetches portlet attributes
        TagCloudPortlet portlet = getPortlet( request.getParameter( PARAMETER_PORTLET_ID ) );
        String strCloudId = request.getParameter( PARAMETER_TAGCLOUD_ID );

        if ( ( portlet == null ) || !isNumeric( strCloudId ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // retrieve portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        // fetches portlet specific attributes
        portlet.setIdCloud( Integer.parseInt( strCloudId ) );

        // updates the portlet
        portlet.update(  );

        TagCloudPortletHome.storeCloud( portlet.getId( ), portlet.getIdCloud( ) );

        // displays the page with the updated portlet
        return getPageUrl( portlet.getPageId(  ) );
    }

    /**
     * Loads a tag cloud portlet from a request parameter, guarding missing, malformed and unknown ids
     *
     * @param strPortletId the portlet id parameter value
     * @return the portlet, or null when the parameter does not name an existing tagcloud portlet
     */
    private TagCloudPortlet getPortlet( String strPortletId )
    {
        if ( !isNumeric( strPortletId ) )
        {
            return null;
        }

        int nPortletId = Integer.parseInt( strPortletId );

        if ( TagCloudPortletHome.getInstance( ).getDAO( ).load( nPortletId ) == null )
        {
            return null;
        }

        Portlet portlet = PortletHome.findByPrimaryKey( nPortletId );

        return ( portlet instanceof TagCloudPortlet ) ? (TagCloudPortlet) portlet : null;
    }

    /**
     * Tells whether a request parameter value is a plain positive integer.
     *
     * @param strValue the parameter value
     * @return true when the value is made of digits only
     */
    private boolean isNumeric( String strValue )
    {
        return ( strValue != null ) && strValue.matches( "[0-9]+" );
    }

    /**
     * Constructs the html combo
     * @param listClouds List of tagClouds present
     * @param strDefaultCloudId The default tagcloud
     * @return A String representation of the combo
     */
    public String getCloudsCombo( ReferenceList listClouds, String strDefaultCloudId )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_TAGCLOUD_LIST, listClouds );
        model.put( MARK_TAGCLOUD_ID, strDefaultCloudId );

        HtmlTemplate templateCombo = AppTemplateService.getTemplate( TEMPLATE_COMBO_CLOUDS, getLocale(  ), model );

        return templateCombo.getHtml(  );
    }
}
