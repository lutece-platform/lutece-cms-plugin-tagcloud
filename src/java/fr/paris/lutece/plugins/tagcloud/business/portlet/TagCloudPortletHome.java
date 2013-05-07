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

import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;


/**
 * This class provides instances management methods for TagCloudPortlet objects
 */
public class TagCloudPortletHome extends PortletHome
{
    // Static variable pointed at the DAO instance
    private static ITagCloudPortletDAO _dao = (ITagCloudPortletDAO) SpringContextService.getPluginBean( "tagcloud",
            "tagCloudPortletDAO" );

    /** This class implements the Singleton design pattern. */
    private static TagCloudPortletHome _singleton = null;

    /**
     * Constructor
    */
    public TagCloudPortletHome(  )
    {
        if ( _singleton == null )
        {
            _singleton = this;
        }
    }

    /**
     * Returns the instance of the TagCloudPortletHome singleton
     *
     * @return the TagCloudPortletHome instance
     */
    public static PortletHome getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new TagCloudPortletHome(  );
        }

        return _singleton;
    }

    /**
     * Returns the type of the portlet
     *
     * @return The type of the portlet
     */
    public String getPortletTypeId(  )
    {
        String strCurrentClassName = this.getClass(  ).getName(  );
        String strPortletTypeId = PortletTypeHome.getPortletTypeId( strCurrentClassName );

        return strPortletTypeId;
    }

    /**
     * Returns the instance of the TagCloudPortletDAO singleton
     *
     * @return the instance of the TagCloudPortletDAO
     */
    public IPortletInterfaceDAO getDAO(  )
    {
        return _dao;
    }

    /**
    * Returns all the sendings associated with a given portlet.
    *
    * @param nPortletId the identifier of the portlet.
    * @return a Set of Integer containing the identifiers.
    */
    public static Collection<Integer> findTagCloudsInPortlet( int nPortletId )
    {
        return _dao.selectTagCloudByPortlet( nPortletId );
    }

    /**
     * Updates association of a portlet to a tagcloud
     * @param nPortletId The id of the portlet
     * @param nCloudId The identifier of the cloud
     */
    public static void storeCloud( int nPortletId, int nCloudId )
    {
        _dao.storeCloud( nPortletId, nCloudId );
    }

    /**
     * Creates association of a portlet to a tagcloud
     * @param nPortletId The id of the portlet
     * @param nCloudId The identifier of the cloud
     */
    public static void insertCloud( int nPortletId, int nCloudId )
    {
        _dao.insertCloud( nPortletId, nCloudId );
    }
}
