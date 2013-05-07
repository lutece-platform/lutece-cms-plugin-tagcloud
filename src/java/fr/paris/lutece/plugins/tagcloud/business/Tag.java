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


/**
 * This is the business class for the object Tag
 */
public class Tag
{
    // Variables declarations 
    private int _nIdTagCloud;
    private int _nIdTag;
    private String _strTagName;
    private String _strTagWeight;
    private String _strTagUrl;

    /**
     * Returns the IdTagCloud
     * @return The IdTagCloud
     */
    public int getIdTagCloud(  )
    {
        return _nIdTagCloud;
    }

    /**
     * Sets the IdTagCloud
     * @param nIdTagCloud The IdTagCloud
     */
    public void setIdTagCloud( int nIdTagCloud )
    {
        _nIdTagCloud = nIdTagCloud;
    }

    /**
     * Returns the IdTag
     * @return The IdTag
     */
    public int getIdTag(  )
    {
        return _nIdTag;
    }

    /**
     * Sets the IdTag
     * @param nTagId The IdTag
     */
    public void setIdTag( int nTagId )
    {
        _nIdTag = nTagId;
    }

    /**
     * Returns the TagName
     * @return The TagName
     */
    public String getTagName(  )
    {
        return _strTagName;
    }

    /**
     * Sets the TagName
     * @param strTagName The TagName
     */
    public void setTagName( String strTagName )
    {
        _strTagName = strTagName;
    }

    /**
     * Returns the TagWeight
     * @return The TagWeight
     */
    public String getTagWeight(  )
    {
        return _strTagWeight;
    }

    /**
     * Sets the TagWeight
     * @param strTagWeight The TagWeight
     */
    public void setTagWeight( String strTagWeight )
    {
        _strTagWeight = strTagWeight;
    }

    /**
     * Returns the TagUrl
     * @return The TagUrl
     */
    public String getTagUrl(  )
    {
        return _strTagUrl;
    }

    /**
     * Sets the TagUrl
     * @param strTagUrl The TagUrl
     */
    public void setTagUrl( String strTagUrl )
    {
        _strTagUrl = strTagUrl;
    }
}
