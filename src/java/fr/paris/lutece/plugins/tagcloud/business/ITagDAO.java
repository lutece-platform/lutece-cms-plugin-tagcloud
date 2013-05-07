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

import java.util.ArrayList;
import java.util.Collection;


/**
* ITagDAO Interface
*/
public interface ITagDAO
{
    /**
     * Insert a new record in the table.
     * @param tag instance of the tag object to inssert
     * @param plugin the Plugin
     */
    void insert( Tag tag, Plugin plugin );

    /**
    * Update the record in the table
    * @param tag the reference of the tag
    * @param plugin the Plugin
    */
    void store( Tag tag, Plugin plugin );

    /**
     * Deletes a Tag
     * @param nIdTag The tag id
     * @param nCloudId The Cloud id
     * @param plugin the Plugin
     */
    void deleteTag( int nIdTag, int nCloudId, Plugin plugin );

    /**
     * Deletes a cloud
     * @param nCloudId The Cloud id
     * @param plugin the Plugin
     */
    void deleteCloud( int nCloudId, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * @return The instance of the tag
     * @param nCloudId The Cloud id
     * @param nTagId The tag id
     * @param plugin the Plugin
     */
    Tag load( int nCloudId, int nTagId, Plugin plugin );

    /**
     * Load by cloud
     * @param nCloudId The Cloud id
     * @param plugin The plugin
     * @return A list of tags
     */
    ArrayList<Tag> loadByCloud( int nCloudId, Plugin plugin );

    /**
    * Load the data of all the tag objects and returns them as a collection
    * @param plugin the Plugin
    * @return The collection which contains the data of all the tags
    */
    Collection<Tag> selectTagList( Plugin plugin );

    /**
     * Select all tag clouds
     * @param plugin The plugin
     * @return A collection of tag clouds
     */
    Collection<TagCloud> selectTagClouds( Plugin plugin );

    /**
     * Select tag cloud by identifier
     * @param nCloudId The Cloud id
     * @param plugin The plugin
     * @return A tagcloud
     */
    TagCloud selectCloudById( int nCloudId, Plugin plugin );

    /**
     * Inserts a tag cloud
     * @param tagCloud The tag cloud
     * @param plugin The plugin
     */
    void insert( TagCloud tagCloud, Plugin plugin );

    /**
     * Stores a tagcloud
     * @param tagCloud The tagcloud object
     * @param plugin The plugin
     */
    void store( TagCloud tagCloud, Plugin plugin );

    /**
     * Selects all tagclouds
     * @param plugin The plugin
     * @return a ReferenceList
     */
    ReferenceList selectAllTagClouds( Plugin plugin );
}
