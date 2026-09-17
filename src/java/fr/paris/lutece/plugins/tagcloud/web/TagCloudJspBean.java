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
package fr.paris.lutece.plugins.tagcloud.web;

import fr.paris.lutece.plugins.tagcloud.business.Tag;
import fr.paris.lutece.plugins.tagcloud.business.TagCloud;
import fr.paris.lutece.plugins.tagcloud.business.TagHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.util.IPager;
import fr.paris.lutece.portal.web.util.Pager;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage tagcloud features ( manage, create, modify, remove )
 */
@RequestScoped
@Named
@Controller( controllerJsp = "ManageTagClouds.jsp", controllerPath = "jsp/admin/plugins/tagcloud/", right = "TAGCLOUD_MANAGEMENT", securityTokenEnabled = true )
public class TagCloudJspBean extends MVCAdminJspBean
{
    // Right
    public static final String RIGHT_MANAGE_TAGCLOUD = "TAGCLOUD_MANAGEMENT";

    private static final long serialVersionUID = 1L;

    private static final String PLUGIN_NAME = "tagcloud";

    // templates
    private static final String TEMPLATE_MANAGE_TAGCLOUD = "/admin/plugins/tagcloud/manage_tagcloud.html";
    private static final String TEMPLATE_MODIFY_TAGCLOUD = "/admin/plugins/tagcloud/modify_tagcloud.html";
    private static final String TEMPLATE_CREATE_TAGCLOUD = "/admin/plugins/tagcloud/create_tagcloud.html";
    private static final String TEMPLATE_CREATE_TAG = "/admin/plugins/tagcloud/create_tag.html";
    private static final String TEMPLATE_MODIFY_TAG = "/admin/plugins/tagcloud/modify_tag.html";

    // properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_TAGCLOUD = "tagcloud.manage_tagcloud.page_title";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_TAGCLOUD = "tagcloud.modify_tagcloud.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_TAGCLOUD = "tagcloud.create_tagcloud.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_TAG = "tagcloud.create_tag.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_TAG = "tagcloud.modify_tag.pageTitle";

    // Views
    private static final String VIEW_MANAGE_TAGCLOUDS = "manageTagClouds";
    private static final String VIEW_CREATE_TAGCLOUD = "createTagCloud";
    private static final String VIEW_MODIFY_TAGCLOUD = "modifyTagCloud";
    private static final String VIEW_CREATE_TAG = "createTag";
    private static final String VIEW_MODIFY_TAG = "modifyTag";
    private static final String VIEW_CONFIRM_REMOVE_TAGCLOUD = "confirmRemoveTagCloud";
    private static final String VIEW_CONFIRM_REMOVE_TAG = "confirmRemoveTag";

    // Actions
    private static final String ACTION_CREATE_TAGCLOUD = "createTagCloud";
    private static final String ACTION_MODIFY_TAGCLOUD = "modifyTagCloud";
    private static final String ACTION_REMOVE_TAGCLOUD = "removeTagCloud";
    private static final String ACTION_CREATE_TAG = "createTag";
    private static final String ACTION_MODIFY_TAG = "modifyTag";
    private static final String ACTION_REMOVE_TAG = "removeTag";

    // Markers
    private static final String MARK_TAG_CLOUD_LIST = "tagcloud_list";
    private static final String MARK_TAGCLOUD_ID = "tagcloud_id";
    private static final String MARK_TAGCLOUD = "tagcloud";
    private static final String MARK_LIST_WEIGHT = "list_weight";
    private static final String MARK_TAG_LIST = "tag_list";
    private static final String MARK_TAG = "tagObject";
    private static final String PARAMETER_TAG_CLOUD_NAME = "tagcloud_name";
    private static final String PARAMETER_TAG_CLOUD_ID = "tagcloud_id";
    private static final String PARAMETER_TAG_NAME = "tag_name";
    private static final String PARAMETER_TAG_URL = "tag_url";
    private static final String PARAMETER_TAG_WEIGHT = "tag_weight";
    private static final String PARAMETER_TAG_ID = "tag_id";

    // Message keys
    private static final String MESSAGE_CONFIRM_REMOVE_TAGCLOUD = "tagcloud.message.confirmRemoveTagCloud";
    private static final String MESSAGE_CONFIRM_REMOVE_TAG = "tagcloud.message.confirmRemoveTag";
    private static final String MESSAGE_OBJECT_NOT_FOUND = "tagcloud.message.objectNotFound";

    // Pager names: PaginatorHandler caches one pager per name for the whole session, and an unnamed
    // pager takes the name of its declaring class, so two pagers in one bean must name themselves apart.
    private static final String PAGER_CLOUDS = "tagcloud.clouds";
    private static final String PAGER_TAGS = "tagcloud.tags";

    @Inject
    @Pager( name = PAGER_CLOUDS, listBookmark = MARK_TAG_CLOUD_LIST )
    private IPager<TagCloud, TagCloud> _cloudPager;

    @Inject
    @Pager( name = PAGER_TAGS, listBookmark = MARK_TAG_LIST )
    private IPager<Tag, Tag> _tagPager;

    /**
     * Returns the list of tagclouds
     *
     * @param request The Http request
     * @param model The model
     * @return the tagclouds list
     */
    @View( value = VIEW_MANAGE_TAGCLOUDS, defaultView = true )
    public String getManageTagClouds( HttpServletRequest request, Models model )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_TAGCLOUD );

        _cloudPager.withListItem( new ArrayList<>( TagHome.getTagClouds( getPlugin( ) ) ) ).populateModels( request, model, getLocale( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_TAGCLOUD, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Returns the form for tag cloud modification
     *
     * @param request The Http request
     * @param model The model
     * @return Html form
     */
    @View( value = VIEW_MODIFY_TAGCLOUD )
    public String getModifyTagCloud( HttpServletRequest request, Models model )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_TAGCLOUD );

        TagCloud tagCloud = findCloud( request.getParameter( PARAMETER_TAG_CLOUD_ID ) );

        if ( tagCloud == null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_OBJECT_NOT_FOUND, AdminMessage.TYPE_STOP ) );
        }

        _tagPager.withListItem( new ArrayList<>( TagHome.findTagsByCloud( tagCloud.getIdTagCloud( ), getPlugin( ) ) ) ).populateModels(
                request, model, getLocale( ) );

        model.put( MARK_TAGCLOUD, tagCloud );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_TAGCLOUD, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Returns the tagcloud creation form
     *
     * @param request The Http request
     * @param model The model
     * @return Html creation form
     */
    @View( value = VIEW_CREATE_TAGCLOUD )
    public String getCreateTagCloud( HttpServletRequest request, Models model )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_TAGCLOUD );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_TAGCLOUD, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Process Tagcloud creation
     *
     * @param request The Http request
     * @return URL
     */
    @Action( ACTION_CREATE_TAGCLOUD )
    public String doCreateTagCloud( HttpServletRequest request )
    {
        String strName = request.getParameter( PARAMETER_TAG_CLOUD_NAME );

        // Mandatory field
        if ( ( strName == null ) || strName.trim( ).isEmpty( ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }

        TagCloud tagcloud = new TagCloud(  );
        tagcloud.setTagCloudDescription( strName );
        TagHome.create( tagcloud, getPlugin( ) );

        return redirectView( request, VIEW_MANAGE_TAGCLOUDS );
    }

    /**
     * Process the tagcloud modifications
     *
     * @param request The Http request
     * @return Html form
     */
    @Action( ACTION_MODIFY_TAGCLOUD )
    public String doModifyTagCloud( HttpServletRequest request )
    {
        String strName = request.getParameter( PARAMETER_TAG_CLOUD_NAME );
        TagCloud tagcloud = findCloud( request.getParameter( PARAMETER_TAG_CLOUD_ID ) );

        // Mandatory field
        if ( ( strName == null ) || strName.trim( ).isEmpty( ) || ( tagcloud == null ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }

        tagcloud.setTagCloudDescription( strName );
        TagHome.update( tagcloud, getPlugin( ) );

        return redirectView( request, VIEW_MANAGE_TAGCLOUDS );
    }

    /**
     * Returns the confirmation to remove the tagcloud
     *
     * @param request The Http request
     * @return the confirmation page
     */
    @View( value = VIEW_CONFIRM_REMOVE_TAGCLOUD, securityTokenAction = ACTION_REMOVE_TAGCLOUD )
    public String getConfirmRemoveTagCloud( HttpServletRequest request )
    {
        TagCloud tagcloud = findCloud( request.getParameter( PARAMETER_TAG_CLOUD_ID ) );

        if ( tagcloud == null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_OBJECT_NOT_FOUND, AdminMessage.TYPE_STOP ) );
        }

        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_TAGCLOUD ) );
        url.addParameter( PARAMETER_TAG_CLOUD_ID, tagcloud.getIdTagCloud( ) );

        return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_TAGCLOUD, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION ) );
    }

    /**
     * Returns the confirmation to remove the tag
     *
     * @param request The Http request
     * @return the confirmation page
     */
    @View( value = VIEW_CONFIRM_REMOVE_TAG, securityTokenAction = ACTION_REMOVE_TAG )
    public String getConfirmRemoveTag( HttpServletRequest request )
    {
        Tag tag = findTag( request.getParameter( PARAMETER_TAG_CLOUD_ID ), request.getParameter( PARAMETER_TAG_ID ) );

        if ( tag == null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_OBJECT_NOT_FOUND, AdminMessage.TYPE_STOP ) );
        }

        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_TAG ) );
        url.addParameter( PARAMETER_TAG_CLOUD_ID, tag.getIdTagCloud( ) );
        url.addParameter( PARAMETER_TAG_ID, tag.getIdTag( ) );

        return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_TAG, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION ) );
    }

    /**
     * Remove a tagcloud
     *
     * @param request The Http request
     * @return Html form
     */
    @Action( ACTION_REMOVE_TAGCLOUD )
    public String doRemoveTagCloud( HttpServletRequest request )
    {
        TagCloud tagcloud = findCloud( request.getParameter( PARAMETER_TAG_CLOUD_ID ) );

        if ( tagcloud == null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_OBJECT_NOT_FOUND, AdminMessage.TYPE_STOP ) );
        }

        List<Tag> listTags = TagHome.findTagsByCloud( tagcloud.getIdTagCloud( ), getPlugin( ) );

        for ( Tag tag : listTags )
        {
            TagHome.removeTag( tag.getIdTag( ), tag.getIdTagCloud( ), getPlugin( ) );
        }

        TagHome.removeCloud( tagcloud.getIdTagCloud( ), getPlugin( ) );

        // Go to the parent page
        return redirectView( request, VIEW_MANAGE_TAGCLOUDS );
    }

    /**
     * Remove a tag
     *
     * @param request The Http request
     * @return Html form
     */
    @Action( ACTION_REMOVE_TAG )
    public String doRemoveTag( HttpServletRequest request )
    {
        Tag tag = findTag( request.getParameter( PARAMETER_TAG_CLOUD_ID ), request.getParameter( PARAMETER_TAG_ID ) );

        if ( tag == null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_OBJECT_NOT_FOUND, AdminMessage.TYPE_STOP ) );
        }

        TagHome.removeTag( tag.getIdTag( ), tag.getIdTagCloud( ), getPlugin( ) );

        return redirect( request, VIEW_MODIFY_TAGCLOUD, PARAMETER_TAG_CLOUD_ID, tag.getIdTagCloud( ) );
    }

    /**
     * Returns the Tag creation form
     *
     * @param request The Http request
     * @param model The model
     * @return Html creation form
     */
    @View( value = VIEW_CREATE_TAG )
    public String getCreateTag( HttpServletRequest request, Models model )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_TAG );

        Integer nCloudId = parseId( request.getParameter( PARAMETER_TAG_CLOUD_ID ) );

        if ( ( nCloudId == null ) || ( TagHome.findCloudById( nCloudId.intValue( ), getPlugin( ) ) == null ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_OBJECT_NOT_FOUND, AdminMessage.TYPE_STOP ) );
        }

        model.put( MARK_TAGCLOUD_ID, nCloudId );
        model.put( MARK_LIST_WEIGHT, getListWeight( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_TAG, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Process Tag creation
     *
     * @param request The Http request
     * @return URL
     */
    @Action( ACTION_CREATE_TAG )
    public String doCreateTag( HttpServletRequest request )
    {
        Integer nCloudId = parseId( request.getParameter( PARAMETER_TAG_CLOUD_ID ) );
        String strTagName = request.getParameter( PARAMETER_TAG_NAME );
        String strTagUrl = request.getParameter( PARAMETER_TAG_URL );
        String strTagWeight = request.getParameter( PARAMETER_TAG_WEIGHT );

        // Mandatory field
        if ( ( nCloudId == null ) || ( strTagName == null ) || strTagName.trim( ).isEmpty( ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }

        Tag tag = new Tag(  );
        tag.setIdTagCloud( nCloudId.intValue( ) );
        tag.setTagName( strTagName );
        tag.setTagUrl( ( strTagUrl != null ) ? strTagUrl.replaceAll( "&", "&amp;" ) : null );
        tag.setTagWeight( strTagWeight );
        TagHome.create( tag, getPlugin( ) );

        return redirect( request, VIEW_MODIFY_TAGCLOUD, PARAMETER_TAG_CLOUD_ID, nCloudId.intValue( ) );
    }

    /**
     * Process the Tag modifications
     *
     * @param request The Http request
     * @return Html form
     */
    @Action( ACTION_MODIFY_TAG )
    public String doModifyTag( HttpServletRequest request )
    {
        Tag tag = findTag( request.getParameter( PARAMETER_TAG_CLOUD_ID ), request.getParameter( PARAMETER_TAG_ID ) );
        String strTagName = request.getParameter( PARAMETER_TAG_NAME );

        // Mandatory field
        if ( ( tag == null ) || ( strTagName == null ) || strTagName.trim( ).isEmpty( ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }

        String strTagUrl = request.getParameter( PARAMETER_TAG_URL );
        String strTagWeight = request.getParameter( PARAMETER_TAG_WEIGHT );
        tag.setTagName( strTagName );
        tag.setTagUrl( ( strTagUrl != null ) ? strTagUrl.replaceAll( "&", "&amp;" ) : null );
        tag.setTagWeight( strTagWeight );
        TagHome.update( tag, getPlugin( ) );

        return redirect( request, VIEW_MODIFY_TAGCLOUD, PARAMETER_TAG_CLOUD_ID, tag.getIdTagCloud( ) );
    }

    /**
     * Returns the form for tag modification
     *
     * @param request The Http request
     * @return Html form
     */
    @View( value = VIEW_MODIFY_TAG )
    public String getModifyTag( HttpServletRequest request, Models model )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_TAG );

        Tag tag = findTag( request.getParameter( PARAMETER_TAG_CLOUD_ID ), request.getParameter( PARAMETER_TAG_ID ) );

        if ( tag == null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_OBJECT_NOT_FOUND, AdminMessage.TYPE_STOP ) );
        }

        model.put( MARK_TAG, tag );
        model.put( MARK_LIST_WEIGHT, getListWeight( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_TAG, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
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

    /**
     * Finds a tag cloud from a request parameter, guarding missing, malformed and unknown ids
     *
     * @param strCloudId the cloud id parameter value
     * @return the cloud, or null when it cannot be resolved
     */
    private TagCloud findCloud( String strCloudId )
    {
        Integer nCloudId = parseId( strCloudId );

        if ( nCloudId == null )
        {
            return null;
        }

        return TagHome.findCloudById( nCloudId.intValue( ), getPlugin( ) );
    }

    /**
     * Finds a tag from request parameters, guarding missing, malformed and unknown ids
     *
     * @param strCloudId the cloud id parameter value
     * @param strTagId the tag id parameter value
     * @return the tag, or null when it cannot be resolved
     */
    private Tag findTag( String strCloudId, String strTagId )
    {
        Integer nCloudId = parseId( strCloudId );
        Integer nTagId = parseId( strTagId );

        if ( ( nCloudId == null ) || ( nTagId == null ) )
        {
            return null;
        }

        return TagHome.findByPrimaryKey( nCloudId.intValue( ), nTagId.intValue( ), getPlugin( ) );
    }

    private Integer parseId( String strValue )
    {
        if ( ( strValue == null ) || !strValue.matches( "[0-9]+" ) )
        {
            return null;
        }

        try
        {
            return Integer.valueOf( strValue );
        }
        catch( NumberFormatException e )
        {
            return null;
        }
    }

}
