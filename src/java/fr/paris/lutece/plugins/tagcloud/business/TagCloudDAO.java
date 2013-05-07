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
package fr.paris.lutece.plugins.tagcloud.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for tag objects
 */
public final class TagCloudDAO implements ITagDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_tag ) FROM tagcloud_tag";
    private static final String SQL_QUERY_NEW_PK_TAGCLOUD = "SELECT max( id_cloud ) FROM tagcloud";
    private static final String SQL_QUERY_SELECT = "SELECT id_cloud, id_tag, tag_name, tag_weight, tag_url FROM tagcloud_tag WHERE id_cloud = ? AND id_tag= ? ";
    private static final String SQL_QUERY_SELECT_BY_CLOUD = "SELECT id_cloud, id_tag, tag_name, tag_weight, tag_url FROM tagcloud_tag WHERE id_cloud = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO tagcloud_tag ( id_cloud, id_tag, tag_name, tag_weight, tag_url ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE_TAG = "DELETE FROM tagcloud_tag WHERE id_tag = ? AND id_cloud =? ";
    private static final String SQL_QUERY_DELETE_CLOUD = "DELETE FROM tagcloud WHERE id_cloud = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE tagcloud_tag SET id_cloud = ?, id_tag = ?, tag_name = ?, tag_weight = ?, tag_url = ? WHERE id_cloud = ? AND id_tag= ? ";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_cloud, id_tag, tag_name, tag_weight, tag_url FROM tagcloud_tag";
    private static final String SQL_QUERY_SELECT_CLOUDS = " select id_cloud, cloud_description FROM tagcloud ";
    private static final String SQL_QUERY_SELECT_CLOUD_BY_ID = " select id_cloud, cloud_description FROM tagcloud WHERE id_cloud= ?";
    private static final String SQL_QUERY_INSERT_TAG_CLOUD = "INSERT INTO tagcloud ( id_cloud,  cloud_description) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_UPDATE_TAGCLOUD = "UPDATE tagcloud SET id_cloud = ?, cloud_description = ? WHERE id_cloud = ?";

    /**
     * Generates a new primary key for a tag
     * @param plugin The plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * Generates a new primary key for the tag cloud
     * @param plugin The plugin
     * @return The new primary key
     */
    public int newPrimaryKeyTagCloud( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK_TAGCLOUD, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * Insert a new record in the table.
     * @param plugin The plugin
     * @param tag instance of the Tag to insert
     */
    public void insert( Tag tag, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        tag.setIdTag( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, tag.getIdTagCloud(  ) );
        daoUtil.setInt( 2, tag.getIdTag(  ) );
        daoUtil.setString( 3, tag.getTagName(  ) );
        daoUtil.setString( 4, tag.getTagWeight(  ) );
        daoUtil.setString( 5, tag.getTagUrl(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of the Tag from the table
     * @return the instance of the Tag
     * @param nCloudId The cloud id
     * @param nTagId The id of the tag
     * @param plugin The plugin
     */
    public Tag load( int nCloudId, int nTagId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nCloudId );
        daoUtil.setInt( 2, nTagId );
        daoUtil.executeQuery(  );

        Tag tag = null;

        if ( daoUtil.next(  ) )
        {
            tag = new Tag(  );

            tag.setIdTagCloud( daoUtil.getInt( 1 ) );
            tag.setIdTag( daoUtil.getInt( 2 ) );
            tag.setTagName( daoUtil.getString( 3 ) );
            tag.setTagWeight( daoUtil.getString( 4 ) );
            tag.setTagUrl( daoUtil.getString( 5 ) );
        }

        daoUtil.free(  );

        return tag;
    }

    /**
     * Load the data of the tag from the table
     * @return the instance of the Tag
     * @param nCloudId The identifier of the cloud
     * @param plugin The plugin
     */
    public ArrayList<Tag> loadByCloud( int nCloudId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CLOUD, plugin );
        daoUtil.setInt( 1, nCloudId );
        daoUtil.executeQuery(  );

        ArrayList<Tag> tagList = new ArrayList<Tag>(  );
        Tag tag = null;

        while ( daoUtil.next(  ) )
        {
            tag = new Tag(  );

            tag.setIdTagCloud( daoUtil.getInt( 1 ) );
            tag.setIdTag( daoUtil.getInt( 2 ) );
            tag.setTagName( daoUtil.getString( 3 ) );
            tag.setTagWeight( daoUtil.getString( 4 ) );
            tag.setTagUrl( daoUtil.getString( 5 ) );

            tagList.add( tag );
        }

        daoUtil.free(  );

        return tagList;
    }

    /**
     * Delete a record from the table
     * @param nCloudId The identifier of the cloud
     * @param plugin The plugin
     * @param nTagId The identifier of the tag
     */
    public void deleteTag( int nTagId, int nCloudId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_TAG, plugin );
        daoUtil.setInt( 1, nTagId );
        daoUtil.setInt( 2, nCloudId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param plugin the Plugin
     * @param tag The reference of the tag
     */
    public void store( Tag tag, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        //Error
        daoUtil.setInt( 1, tag.getIdTagCloud(  ) );
        daoUtil.setInt( 2, tag.getIdTag(  ) );
        daoUtil.setString( 3, tag.getTagName(  ) );
        daoUtil.setString( 4, tag.getTagWeight(  ) );
        daoUtil.setString( 5, tag.getTagUrl(  ) );
        daoUtil.setInt( 6, tag.getIdTagCloud(  ) );
        daoUtil.setInt( 7, tag.getIdTag(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the tags and returns them as a collection
     * @return The Collection which contains the data of all the tags
     * @param plugin The plugin
     */
    public Collection<Tag> selectTagList( Plugin plugin )
    {
        Collection<Tag> tagList = new ArrayList<Tag>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Tag tag = new Tag(  );

            tag.setIdTagCloud( daoUtil.getInt( 1 ) );
            tag.setIdTag( daoUtil.getInt( 2 ) );
            tag.setTagName( daoUtil.getString( 3 ) );
            tag.setTagWeight( daoUtil.getString( 4 ) );
            tag.setTagUrl( daoUtil.getString( 5 ) );

            tagList.add( tag );
        }

        daoUtil.free(  );

        return tagList;
    }

    /**
     * Load the data of all the tagClouds and returns them as a collection
     * @return The Collection which contains the data of all the tagClouds
     * @param plugin The Plugin
     */
    public Collection<TagCloud> selectTagClouds( Plugin plugin )
    {
        Collection<TagCloud> tagCloudList = new ArrayList<TagCloud>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CLOUDS, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            TagCloud tagCloud = new TagCloud(  );

            tagCloud.setIdTagCloud( daoUtil.getInt( 1 ) );
            tagCloud.setTagCloudDescription( daoUtil.getString( 2 ) );

            tagCloudList.add( tagCloud );
        }

        daoUtil.free(  );

        return tagCloudList;
    }

    /**
     * Select a tagcloud by its identifier
     * @param nCloudId The cloud identifier
     * @param plugin The plugin object
     * @return The tagcloud
     */
    public TagCloud selectCloudById( int nCloudId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CLOUD_BY_ID, plugin );
        daoUtil.setInt( 1, nCloudId );
        daoUtil.executeQuery(  );

        TagCloud tagCloud = null;

        if ( daoUtil.next(  ) )
        {
            tagCloud = new TagCloud(  );
            tagCloud.setIdTagCloud( daoUtil.getInt( 1 ) );
            tagCloud.setTagCloudDescription( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return tagCloud;
    }

    /**
     * Creates a new TagCloud
     * @param tagCloud The instance of the tagcloud
     * @param plugin The plugin
     */
    public void insert( TagCloud tagCloud, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_TAG_CLOUD, plugin );

        tagCloud.setIdTagCloud( newPrimaryKeyTagCloud( plugin ) );

        daoUtil.setInt( 1, tagCloud.getIdTagCloud(  ) );
        daoUtil.setString( 2, tagCloud.getTagCloudDescription(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
    * Updates the record in the table
    * @param tagCloud The tagcloud
    * @param plugin The plugin
    */
    public void store( TagCloud tagCloud, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_TAGCLOUD, plugin );

        daoUtil.setInt( 1, tagCloud.getIdTagCloud(  ) );
        daoUtil.setString( 2, tagCloud.getTagCloudDescription(  ) );
        daoUtil.setInt( 3, tagCloud.getIdTagCloud(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
    * Delete a record from the table
    * @param nCloudId The id of the tag cloud
    * @param plugin The plugin
    */
    public void deleteCloud( int nCloudId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_CLOUD, plugin );
        daoUtil.setInt( 1, nCloudId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
    * Load the list of clouds
    * @return A referenceList representing the TagClouds
    * @param plugin The plugin
    */
    public ReferenceList selectAllTagClouds( Plugin plugin )
    {
        ReferenceList listClouds = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CLOUDS, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            TagCloud tag = new TagCloud(  );
            tag.setIdTagCloud( daoUtil.getInt( 1 ) );
            tag.setTagCloudDescription( daoUtil.getString( 2 ) );
            listClouds.addItem( tag.getIdTagCloud(  ), tag.getTagCloudDescription(  ) );
        }

        daoUtil.free(  );

        return listClouds;
    }
}
