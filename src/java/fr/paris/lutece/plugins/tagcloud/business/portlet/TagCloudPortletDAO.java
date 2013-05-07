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

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for TagCloudPortlet objects
 */
public final class TagCloudPortletDAO implements ITagCloudPortletDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_portlet, id_cloud FROM tagcloud_portlet WHERE id_portlet = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO tagcloud_portlet ( id_portlet, id_cloud ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM tagcloud_portlet WHERE id_portlet = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE tagcloud_portlet SET id_portlet = ?, id_cloud = ? WHERE id_portlet = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_portlet, id_cloud FROM tagcloud_portlet";
    private static final String SQL_QUERY_SELECT_TAGCLOUD_BY_PORTLET = "SELECT id_cloud FROM tagcloud_portlet WHERE id_portlet = ?";

    /**
     * Insert a new record in the table.
     *
     * @param tagCloudPortlet instance of the TagCloudPortlet object to insert
     */
    public void insert( TagCloudPortlet tagCloudPortlet )
    {
    }

    /**
     * Load the data of the tagCloudPortlet from the table
     *
     * @return the instance of the TagCloudPortlet
     * @param nId The identifier of the tagCloudPortlet
     */
    public TagCloudPortlet load( int nId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        TagCloudPortlet tagCloudPortlet = null;

        if ( daoUtil.next(  ) )
        {
            tagCloudPortlet = new TagCloudPortlet(  );

            tagCloudPortlet.setIdPortlet( daoUtil.getInt( 1 ) );
            tagCloudPortlet.setIdCloud( daoUtil.getInt( 2 ) );
        }

        daoUtil.free(  );

        return tagCloudPortlet;
    }

    /**
     * Delete a record from the table
     *
     * @param nTagCloudPortletId The identifier of the tagCloudPortlet
     */
    public void delete( int nTagCloudPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nTagCloudPortletId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param tagCloudPortlet The reference of the tagCloudPortlet
     */
    public void store( TagCloudPortlet tagCloudPortlet )
    {
    }

    /**
     * Load the data of all the tagCloudPortlets and returns them as a collection
     *
     * @return The Collection which contains the data of all the tagCloudPortlets
     */
    public Collection<TagCloudPortlet> selectTagCloudPortletsList(  )
    {
        Collection<TagCloudPortlet> tagCloudPortletList = new ArrayList<TagCloudPortlet>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            TagCloudPortlet tagCloudPortlet = new TagCloudPortlet(  );

            tagCloudPortlet.setIdPortlet( daoUtil.getInt( 1 ) );
            tagCloudPortlet.setIdCloud( daoUtil.getInt( 2 ) );

            tagCloudPortletList.add( tagCloudPortlet );
        }

        daoUtil.free(  );

        return tagCloudPortletList;
    }

    /**
     * A collection of integer
     * @param nPortletId The portlet id
     * @return A collection
     */
    public Collection<Integer> selectTagCloudByPortlet( int nPortletId )
    {
        Collection<Integer> tagCloudPortletList = new ArrayList<Integer>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_TAGCLOUD_BY_PORTLET );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            tagCloudPortletList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return tagCloudPortletList;
    }

    /**
     * Associates a portlet to a tagcloud
     * @param nPortletId The portlet id
     * @param nCloudId The cloud id
     */
    public void insertCloud( int nPortletId, int nCloudId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        daoUtil.setInt( 1, nPortletId );
        daoUtil.setInt( 2, nCloudId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
    * Updates association of a portlet to a tagcloud
    * @param nPortletId The portlet id
    * @param nCloudId The cloud id
    */
    public void storeCloud( int nPortletId, int nCloudId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setInt( 1, nPortletId );
        daoUtil.setInt( 2, nCloudId );
        daoUtil.setInt( 3, nPortletId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Associates a portlet to a tagcloud
     * @param portlet The portlet
     */
    public void insert( Portlet portlet )
    {
        TagCloudPortlet port = (TagCloudPortlet) portlet;
        insertCloud( port.getId(  ), port.getIdCloud(  ) );
    }

    /**
     * A dummy store method
     * @param portlet The portlet
     */
    public void store( Portlet portlet )
    {
        //Not implemented
    }
}
