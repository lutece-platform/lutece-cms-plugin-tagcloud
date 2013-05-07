/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage tagcloud Portlet features
 */
public class TagCloudPortletJspBean extends PortletJspBean
{
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
        HtmlTemplate template = getCreateTemplate( strPageId, strPortletTypeId );

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
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        TagCloudPortlet portlet = (TagCloudPortlet) PortletHome.findByPrimaryKey( nPortletId );
        HtmlTemplate template = getModifyTemplate( portlet );

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
        TagCloudPortlet portlet = new TagCloudPortlet(  );

        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        int nIdPage = Integer.parseInt( strIdPage );

        //gets the identifier of the parent page
        int nCloudId = Integer.parseInt( request.getParameter( PARAMETER_TAGCLOUD_ID ) );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        portlet.setPageId( nIdPage );

        //gets the specific parameters
        portlet.setIdCloud( nCloudId );

        //Portlet creation
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
        // fetches portlet attributes
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        TagCloudPortlet portlet = (TagCloudPortlet) PortletHome.findByPrimaryKey( nPortletId );

        // retrieve portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        // fetches portlet specific attributes
        String strCloudId = request.getParameter( PARAMETER_TAGCLOUD_ID );
        portlet.setIdCloud( Integer.parseInt( strCloudId ) );

        // updates the portlet
        portlet.update(  );

        int nCloudId = Integer.parseInt( request.getParameter( PARAMETER_TAGCLOUD_ID ) );
        TagCloudPortletHome.storeCloud( nPortletId, nCloudId );

        // displays the page with the updated portlet
        return getPageUrl( portlet.getPageId(  ) );
    }

    /**
     * Constructs the html combo
     * @param listClouds List of tagClouds present
     * @param strDefaultCloudId The default tagcloud
     * @return A String representation of the combo
     */
    public String getCloudsCombo( ReferenceList listClouds, String strDefaultCloudId )
    {
        HashMap model = new HashMap(  );
        model.put( MARK_TAGCLOUD_LIST, listClouds );
        model.put( MARK_TAGCLOUD_ID, strDefaultCloudId );

        HtmlTemplate templateCombo = AppTemplateService.getTemplate( TEMPLATE_COMBO_CLOUDS, getLocale(  ), model );

        return templateCombo.getHtml(  );
    }
}
