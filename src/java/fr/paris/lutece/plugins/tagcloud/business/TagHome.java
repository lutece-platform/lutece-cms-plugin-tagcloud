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
package fr.paris.lutece.plugins.tagcloud.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for Tags objects
 */
public final class TagHome
{
    // Static variable pointed at the DAO instance
    private static ITagDAO _dao = (ITagDAO) SpringContextService.getPluginBean( "tagcloud", "tagsDAO" );

    /**
     * Private constructor - this class needs not be instantiated
     */
    private TagHome(  )
    {
    }

    /**
     * Create an instance of the tags class
     * @param tag The tag
     * @param plugin the Plugin
     * @return The  instance of tags which has been created with its primary key.
     */
    public static Tag create( Tag tag, Plugin plugin )
    {
        _dao.insert( tag, plugin );

        return tag;
    }

    /**
     * Update of the tags which is specified in parameter
     * @param tags The instance of the Tags which contains the data to store
     * @param plugin the Plugin
     * @return The instance of the  tags which has been updated
     */
    public static Tag update( Tag tags, Plugin plugin )
    {
        _dao.store( tags, plugin );

        return tags;
    }

    /**
     * Remove the tags whose identifier is specified in parameter
     * @param nCloudId The cloud if
     * @param nTagId The tags Id
     * @param plugin the Plugin
     */
    public static void removeTag( int nTagId, int nCloudId, Plugin plugin )
    {
        _dao.deleteTag( nTagId, nCloudId, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Tag instance whose identifier is specified in parameter
     * @param nCloudId The cloud id
     * @param nTagId The tag identifier
     * @param plugin the Plugin
     * @return Instance of tags
     */
    public static Tag findByPrimaryKey( int nCloudId, int nTagId, Plugin plugin )
    {
        return _dao.load( nCloudId, nTagId, plugin );
    }

    /**
     * Finds a list of tags by cloud
     * @param nCloudId the id of the cloud
     * @param plugin The plugin
     * @return an ArrayList
     */
    public static ArrayList<Tag> findTagsByCloud( int nCloudId, Plugin plugin )
    {
        return _dao.loadByCloud( nCloudId, plugin );
    }

    /**
     * Load the data of all the tags objects and returns them in form of a collection
     * @param plugin the Plugin
     * @return the collection which contains the data of all the tags objects
     */
    public static Collection<Tag> getTagList( Plugin plugin )
    {
        return _dao.selectTagList( plugin );
    }

    /**
    * Load the data of all the tags objects and returns them in form of a collection
    * @param plugin the Plugin
    * @return the collection which contains the data of all the tags objects
    */
    public static Collection<TagCloud> getTagClouds( Plugin plugin )
    {
        return _dao.selectTagClouds( plugin );
    }

    /**
     * A list of tag clouds
     * @param plugin The plugin
     * @return a ReferenceList
     */
    public static ReferenceList getAllTagClouds( Plugin plugin )
    {
        return _dao.selectAllTagClouds( plugin );
    }

    /**
     * Load the data of all the tags objects and returns them in form of a collection
     * @param nCloudId The cloud id
     * @param plugin the Plugin
     * @return the collection which contains the data of all the tags objects
     */
    public static TagCloud findCloudById( int nCloudId, Plugin plugin )
    {
        return _dao.selectCloudById( nCloudId, plugin );
    }

    /**
     * Creates a nes tagcloud
     * @param tagCloud The tagcloud
     * @param plugin The plugin
     * @return The tagcloud
     */
    public static TagCloud create( TagCloud tagCloud, Plugin plugin )
    {
        _dao.insert( tagCloud, plugin );

        return tagCloud;
    }

    /**
     * Update of the tagCloud which is specified in parameter
     *
     * @return The instance of the  tags which has been updated
     * @param tagCloud The tagcloud
     * @param plugin the Plugin
     */
    public static TagCloud update( TagCloud tagCloud, Plugin plugin )
    {
        _dao.store( tagCloud, plugin );

        return tagCloud;
    }

    /**
     * Removes the cloud
     * @param nCloudId The identifier of the cloud
     * @param plugin The plugin
     */
    public static void removeCloud( int nCloudId, Plugin plugin )
    {
        _dao.deleteCloud( nCloudId, plugin );
    }
}
