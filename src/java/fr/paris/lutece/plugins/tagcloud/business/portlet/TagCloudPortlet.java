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
package fr.paris.lutece.plugins.tagcloud.business.portlet;

import fr.paris.lutece.plugins.tagcloud.business.Tag;
import fr.paris.lutece.plugins.tagcloud.business.TagHome;
import fr.paris.lutece.plugins.tagcloud.service.RandomTagService;
import fr.paris.lutece.portal.business.portlet.PortletHtmlContent;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;


/**
 * This class represents business objects TagCloudPortlet
 */
public class TagCloudPortlet extends PortletHtmlContent
{
    private static final String TEMPLATE_PORTLET_TAGCLOUD = "skin/plugins/tagcloud/portlet/tagcloud_portlet.html";
    private static final String MARK_TAGCLOUDS = "tagclouds";
    private static final String MARK_PORTLET_NAME = "portlet_name";
    private static final String MARK_PORTLET_ID = "portlet_id";
    private static final String MARK_TAGS = "tags";
    private static final String MARK_ID = "id";
    private static final String PLUGIN_NAME = "tagcloud";

    // Variables declarations
    private int _nIdPortlet;
    private int _nIdCloud;

    /**
     * Sets the identifier of the portlet type to value specified
     */
    public TagCloudPortlet(  )
    {
        setPortletTypeId( TagCloudPortletHome.getInstance(  ).getPortletTypeId(  ) );
    }

    /**
     * Returns the HTML content of the TagCloud portlet
     *
     * @param request The HTTP servlet request
     * @return the HTML code of the TagCloud portlet content
     */
    @Override
    public String getHtmlContent( HttpServletRequest request )
    {
        Plugin plugin = PluginService.getPlugin( PLUGIN_NAME );
        List<Map<String, Object>> listClouds = new ArrayList<>( );

        Collection<Integer> listCloudIds = TagCloudPortletHome.findTagCloudsInPortlet( this.getId( ) );

        for ( Integer nCloudId : listCloudIds )
        {
            ArrayList<Tag> listTags = TagHome.findTagsByCloud( nCloudId.intValue( ), plugin );

            if ( ( listTags != null ) && !listTags.isEmpty( ) )
            {
                listTags = new RandomTagService( ).transform( listTags );
            }

            Map<String, Object> cloudModel = new HashMap<>( );
            cloudModel.put( MARK_ID, nCloudId );
            cloudModel.put( MARK_TAGS, ( listTags != null ) ? listTags : Collections.emptyList( ) );
            listClouds.add( cloudModel );
        }

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_TAGCLOUDS, listClouds );
        model.put( MARK_PORTLET_ID, getId( ) );

        if ( getDisplayPortletTitle( ) == 0 )
        {
            model.put( MARK_PORTLET_NAME, getName( ) );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PORTLET_TAGCLOUD, getLocale( request ), model );

        return template.getHtml( );
    }

    /**
     * Updates the current instance of the TagCloud portlet object
     */
    public void update(  )
    {
        TagCloudPortletHome.getInstance(  ).update( this );
    }

    /**
     * Removes the current instance of the TagCloud object
     */
    public void remove(  )
    {
        TagCloudPortletHome.getInstance(  ).remove( this );
    }

    /**
     * Returns the IdPortlet
     * @return The IdPortlet
     */
    public int getIdPortlet(  )
    {
        return _nIdPortlet;
    }

    /**
     * Sets the IdPortlet
     * @param nIdPortlet The IdPortlet
     */
    public void setIdPortlet( int nIdPortlet )
    {
        _nIdPortlet = nIdPortlet;
    }

    /**
     * Returns the IdCloud
     * @return The IdCloud
     */
    public int getIdCloud(  )
    {
        return _nIdCloud;
    }

    /**
     * Sets the IdCloud
     * @param nIdCloud The IdCloud
     */
    public void setIdCloud( int nIdCloud )
    {
        _nIdCloud = nIdCloud;
    }
}
