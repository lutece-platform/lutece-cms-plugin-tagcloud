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
package fr.paris.lutece.plugins.tagcloud.business.portlet;

import fr.paris.lutece.plugins.tagcloud.business.Tag;
import fr.paris.lutece.plugins.tagcloud.business.TagHome;
import fr.paris.lutece.plugins.tagcloud.service.RandomTagService;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business objects TagCloudPortlet
 */
public class TagCloudPortlet extends Portlet
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String TAG_WEIGHT = "tag-weight";
    private static final String TAG_NAME = "tag-name";
    private static final String TAG_URL = "tag-url";
    private static final String TAG_PORTLET_TAGCLOUD = "cloud";
    private static final String TAG = "tag";

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
     * Returns the Xml code of the TagCloud portlet with XML heading
     *
     * @param request The HTTP servlet request
     * @return the Xml code of the TagCloud portlet
     */
    public String getXmlDocument( HttpServletRequest request )
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * Returns the Xml code of the TagCloud portlet without XML heading
     *
     * @param request The HTTP servlet request
     * @return the Xml code of the TagCloud portlet content
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer strXml = new StringBuffer(  );
        Plugin plugin = PluginService.getPlugin( "tagcloud" );

        //Fetch the cloud id from the portlet
        Collection<Integer> listClouds = TagCloudPortletHome.findTagCloudsInPortlet( this.getId(  ) );
        XmlUtil.beginElement( strXml, TAG_PORTLET_TAGCLOUD );

        for ( Integer tagCloudId : listClouds )
        {
            ArrayList<Tag> listTags = TagHome.findTagsByCloud( tagCloudId.intValue(  ), plugin );
            RandomTagService service = new RandomTagService(  );

            //If no tag is present in a tag 
            if ( ( listTags != null ) && !listTags.isEmpty(  ) )
            {
                listTags = service.transform( listTags );

                for ( Tag tag : listTags )
                {
                    XmlUtil.beginElement( strXml, TAG );
                    XmlUtil.addElement( strXml, TAG_NAME, tag.getTagName(  ) );
                    XmlUtil.addElement( strXml, TAG_URL, tag.getTagUrl(  ) );
                    XmlUtil.addElement( strXml, TAG_WEIGHT, tag.getTagWeight(  ) );
                    XmlUtil.endElement( strXml, TAG );
                }
            }
            else
            {
                XmlUtil.beginElement( strXml, TAG );
                XmlUtil.addElement( strXml, TAG_NAME, "" );
                XmlUtil.addElement( strXml, TAG_URL, "" );
                XmlUtil.addElement( strXml, TAG_WEIGHT, "" );
                XmlUtil.endElement( strXml, TAG );
            }
        }

        XmlUtil.endElement( strXml, TAG_PORTLET_TAGCLOUD );

        /**/
        return addPortletTags( strXml );
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
