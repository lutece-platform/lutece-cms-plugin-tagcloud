/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
package fr.paris.lutece.plugins.tagcloud.web;

import fr.paris.lutece.plugins.tagcloud.business.Tag;
import fr.paris.lutece.plugins.tagcloud.business.TagCloud;
import fr.paris.lutece.plugins.tagcloud.business.TagHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage tagcloud features ( manage, create, modify, remove )
 */
public class TagCloudJspBean extends PluginAdminPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_TAGCLOUD = "TAGCLOUD_MANAGEMENT";

    // parameters

    // templates
    private static final String TEMPLATE_MANAGE_TAGCLOUD = "/admin/plugins/tagcloud/manage_tagcloud.html";
    private static final String TEMPLATE_MODIFY_TAGCLOUD = "/admin/plugins/tagcloud/modify_tagcloud.html";
    private static final String TEMPLATE_CREATE_TAGCLOUD = "/admin/plugins/tagcloud/create_tagcloud.html";
    private static final String TEMPLATE_CREATE_TAG = "/admin/plugins/tagcloud/create_tag.html";
    private static final String TEMPLATE_MODIFY_TAG = "/admin/plugins/tagcloud/modify_tag.html";

    // properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_TAGCLOUD = "tagcloud.manage_tagcloud.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_TAGCLOUD = "tagcloud.modify_tagcloud.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_TAGCLOUD = "tagcloud.create_tagcloud.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_TAG = "tagcloud.create_tag.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_TAG = "tagcloud.modify_tag.pageTitle";
    private static final String PROPERTY_PLUGIN_NAME = "tagcloud";

    // Markers
    private static final String JSP_DO_REMOVE_TAGCLOUD = "jsp/admin/plugins/tagcloud/DoRemoveTagCloud.jsp";
    private static final String JSP_DO_REMOVE_TAG = "jsp/admin/plugins/tagcloud/DoRemoveTag.jsp";
    private static final String MARK_TAG_CLOUD_LIST = "tagcloud_list";
    private static final String MARK_TAGCLOUD_ID = "tagcloud_id";
    private static final String MARK_TAGCLOUD = "tagcloud";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_LIST_WEIGHT = "list_weight";
    private static final String JSP_MODIFY_TAG_CLOUD = "jsp/admin/plugins/tagcloud/ModifyTagCloud.jsp";
    private static final String JSP_MODIFY_HOME = "ModifyTagCloud.jsp";
    private static final String MARK_TAG_LIST = "tag_list";
    private static final String MARK_TAG = "tag";
    private static final String PROPERTY_TAGS_PER_PAGE = "tagcloud.eventsPerPage";
    private static final String PARAMETER_PLUGIN_NAME = "plugin_name";
    private static final String PARAMETER_TAG_CLOUD_NAME = "tagcloud_name";
    private static final String PARAMETER_TAG_CLOUD_ID = "tagcloud_id";
    private static final String PARAMETER_TAG_NAME = "tag_name";
    private static final String PARAMETER_TAG_URL = "tag_url";
    private static final String PARAMETER_TAG_WEIGHT = "tag_weight";
    private static final String PARAMETER_TAG_ID = "tag_id";

    // Message keys
    private static final String MESSAGE_CONFIRM_REMOVE_TAGCLOUD = "tagcloud.message.confirmRemoveTagCloud";
    private static final String MESSAGE_CONFIRM_REMOVE_TAG = "tagcloud.message.confirmRemoveTag";
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;

    // Jsp Definition

    // Messages

    /**
     * This class is used to manage tag clouds
     */
    public TagCloudJspBean(  )
    {
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_TAGS_PER_PAGE, 5 );
    }

    /**
     * Returns the list of tagclouds
     *
     * @param request The Http request
     * @return the tagclouds list
     */
    public String getManageTagClouds( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_TAGCLOUD );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        HashMap model = new HashMap(  );
        List listClouds = (List) TagHome.getTagClouds( getPlugin(  ) );
        Paginator paginator = new Paginator( listClouds, _nItemsPerPage, getHomeUrl( request ),
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        model.put( MARK_TAG_CLOUD_LIST, listClouds );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_TAGCLOUD, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the form for tag cloud modification
     *
     * @param request The Http request
     * @return Html form
     */
    public String getModifyTagCloud( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_TAGCLOUD );

        int nCloudId = Integer.parseInt( request.getParameter( PARAMETER_TAG_CLOUD_ID ) );
        TagCloud tagCloud = TagHome.findCloudById( nCloudId, getPlugin(  ) );
        HashMap model = new HashMap(  );
        List listTags = TagHome.findTagsByCloud( nCloudId, getPlugin(  ) );

        //paginator parameters
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        UrlItem url = new UrlItem( JSP_MODIFY_TAG_CLOUD );
        url.addParameter( PARAMETER_PLUGIN_NAME, PROPERTY_PLUGIN_NAME );
        url.addParameter( PARAMETER_TAG_CLOUD_ID, nCloudId );

        Paginator paginator = new Paginator( listTags, _nItemsPerPage, url.getUrl(  ), Paginator.PARAMETER_PAGE_INDEX,
                _strCurrentPageIndex );
        model.put( MARK_TAGCLOUD, tagCloud );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_TAG_LIST, paginator.getPageItems(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_TAGCLOUD, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the tagcloud creation form
     *
     * @param request The Http request
     * @return Html creation form
     */
    public String getCreateTagCloud( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_TAGCLOUD );

        HashMap model = new HashMap(  );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_TAGCLOUD, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process Tagcloud creation
     *
     * @param request The Http request
     * @return URL
     */
    public String doCreateTagCloud( HttpServletRequest request )
    {
        String strName = request.getParameter( PARAMETER_TAG_CLOUD_NAME );

        // Mandatory field
        if ( strName.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        TagCloud tagcloud = new TagCloud(  );
        tagcloud.setTagCloudDescription( strName );
        TagHome.create( tagcloud, getPlugin(  ) );

        return getHomeUrl( request );
    }

    /**
     * Process the tagcloud modifications
     *
     * @param request The Http request
     * @return Html form
     */
    public String doModifyTagCloud( HttpServletRequest request )
    {
        String strName = request.getParameter( PARAMETER_TAG_CLOUD_NAME );

        // Mandatory field
        int nCloudId = Integer.parseInt( request.getParameter( PARAMETER_TAG_CLOUD_ID ) );

        if ( strName.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        TagCloud tagcloud = TagHome.findCloudById( nCloudId, getPlugin(  ) );
        tagcloud.setTagCloudDescription( strName );
        TagHome.update( tagcloud, getPlugin(  ) );

        return getHomeUrl( request );
    }

    /**
     * Returns the confirmation to remove the tagcloud
     *
     * @param request The Http request
     * @return the confirmation page
     */
    public String getConfirmRemoveTagCloud( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( JSP_DO_REMOVE_TAGCLOUD );
        url.addParameter( PARAMETER_TAG_CLOUD_ID, Integer.parseInt( request.getParameter( PARAMETER_TAG_CLOUD_ID ) ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_TAGCLOUD, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Returns the confirmation to remove the tag
     *
     * @param request The Http request
     * @return the confirmation page
     */
    public String getConfirmRemoveTag( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( JSP_DO_REMOVE_TAG );
        url.addParameter( PARAMETER_TAG_CLOUD_ID, Integer.parseInt( request.getParameter( PARAMETER_TAG_CLOUD_ID ) ) );
        url.addParameter( PARAMETER_TAG_ID, Integer.parseInt( request.getParameter( PARAMETER_TAG_ID ) ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_TAG, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Remove a tagcloud
     *
     * @param request The Http request
     * @return Html form
     */
    public String doRemoveTagCloud( HttpServletRequest request )
    {
        int nCloudId = Integer.parseInt( request.getParameter( PARAMETER_TAG_CLOUD_ID ) );
        List<Tag> listTags = TagHome.findTagsByCloud( nCloudId, getPlugin(  ) );

        for ( Tag tag : listTags )
        {
            TagHome.removeTag( tag.getIdTag(  ), tag.getIdTagCloud(  ), getPlugin(  ) );
        }

        TagHome.removeCloud( nCloudId, getPlugin(  ) );

        // Go to the parent page
        return getHomeUrl( request );
    }

    /**
     * Remove a tag
     *
     * @param request The Http request
     * @return Html form
     */
    public String doRemoveTag( HttpServletRequest request )
    {
        int nCloudId = Integer.parseInt( request.getParameter( PARAMETER_TAG_CLOUD_ID ) );
        int nTagId = Integer.parseInt( request.getParameter( PARAMETER_TAG_ID ) );

        TagHome.removeTag( nTagId, nCloudId, getPlugin(  ) );

        UrlItem url = new UrlItem( JSP_MODIFY_HOME );
        url.addParameter( PARAMETER_TAG_CLOUD_ID, nCloudId );

        return url.getUrl(  );
    }

    /**
     * Returns the Tag creation form
     *
     * @param request The Http request
     * @return Html creation form
     */
    public String getCreateTag( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_TAG );

        HashMap model = new HashMap(  );
        model.put( MARK_TAGCLOUD_ID, Integer.parseInt( request.getParameter( PARAMETER_TAG_CLOUD_ID ) ) );
        model.put( MARK_LIST_WEIGHT, getListWeight(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_TAG, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process Tag creation
     *
     * @param request The Http request
     * @return URL
     */
    public String doCreateTag( HttpServletRequest request )
    {
        int nCloudId = Integer.parseInt( request.getParameter( PARAMETER_TAG_CLOUD_ID ) );
        String strTagName = request.getParameter( PARAMETER_TAG_NAME );
        String strTagUrl = request.getParameter( PARAMETER_TAG_URL );
        String strTagWeight = request.getParameter( PARAMETER_TAG_WEIGHT );

        // Mandatory field
        if ( ( strTagName == null ) || strTagName.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        Tag tag = new Tag(  );
        tag.setIdTagCloud( nCloudId );
        tag.setTagName( strTagName );
        tag.setTagUrl( strTagUrl.replaceAll( "&", "&amp;" ) );
        tag.setTagWeight( strTagWeight );
        TagHome.create( tag, getPlugin(  ) );

        UrlItem url = new UrlItem( JSP_MODIFY_HOME );
        url.addParameter( PARAMETER_TAG_CLOUD_ID, nCloudId );

        return url.getUrl(  );
    }

    /**
     * Process the Tag modifications
     *
     * @param request The Http request
     * @return Html form
     */
    public String doModifyTag( HttpServletRequest request )
    {
        int nCloudId = Integer.parseInt( request.getParameter( PARAMETER_TAG_CLOUD_ID ) );
        int nTagId = Integer.parseInt( request.getParameter( PARAMETER_TAG_ID ) );
        String strTagName = request.getParameter( PARAMETER_TAG_NAME );

        // Mandatory field
        if ( ( strTagName == null ) || strTagName.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        Tag tag = TagHome.findByPrimaryKey( nCloudId, nTagId, getPlugin(  ) );

        String strTagUrl = request.getParameter( PARAMETER_TAG_URL );
        String strTagWeight = request.getParameter( PARAMETER_TAG_WEIGHT );
        tag.setTagName( strTagName );
        tag.setTagUrl( strTagUrl.replaceAll( "&", "&amp;" ) );
        tag.setTagWeight( strTagWeight );
        TagHome.update( tag, getPlugin(  ) );

        UrlItem url = new UrlItem( JSP_MODIFY_HOME );
        url.addParameter( PARAMETER_TAG_CLOUD_ID, nCloudId );

        return url.getUrl(  );
    }

    /**
     * Returns the form for tag modification
     *
     * @param request The Http request
     * @return Html form
     */
    public String getModifyTag( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_TAG );

        int nCloudId = Integer.parseInt( request.getParameter( PARAMETER_TAG_CLOUD_ID ) );
        int nTagId = Integer.parseInt( request.getParameter( PARAMETER_TAG_ID ) );
        Tag tag = TagHome.findByPrimaryKey( nCloudId, nTagId, getPlugin(  ) );

        HashMap model = new HashMap(  );
        model.put( MARK_TAG, tag );
        model.put( MARK_LIST_WEIGHT, getListWeight(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_TAG, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Creates a list from 1 to 10 corresponding to weight of tags
     * @return a reference list
     */
    private ReferenceList getListWeight(  )
    {
        ReferenceList listWeight = new ReferenceList(  );

        for ( int i = 1; i < 11; i++ )
        {
            listWeight.addItem( "" + i, "" + i );
        }

        return listWeight;
    }
}
