/*
 * Copyright (c) 2002-2026, Mairie de Paris
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletTemplate;
import fr.paris.lutece.portal.business.portlet.PortletTemplateHome;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;

/**
 * Renders a tag cloud portlet with every shipped FreeMarker template
 */
public class TagCloudPortletRenderingTest extends LuteceTestCase
{
    private static final String PLUGIN_NAME = "tagcloud";
    private static final String PORTLET_NAME = "TagCloudPortletRenderingTest title";
    private static final String MARKER_PORTLET = "portlet-tagcloud";
    // Sample cloud "Open Source Tools" and one of its tags (init_tagcloud_sample.sql)
    private static final int SAMPLE_CLOUD_ID = 1;
    private static final String SAMPLE_TAG_NAME = "Freemarker";
    private static final String SAMPLE_TAG_URL = "http://freemarker.sourceforge.net";
    private static final String SAMPLE_TAG_CLASS = "tag8";
    private static final int SHIPPED_TEMPLATES_COUNT = 2;
    private static final int UNKNOWN_TEMPLATE_ID = 99999;

    private TagCloudPortlet _portlet;

    @BeforeEach
    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );

        // The portlet types are registered when the plugin is installed from the back office, which the test bootstrap does not do
        for ( PortletType portletType : PluginService.getPlugin( PLUGIN_NAME ).getPortletTypes( ) )
        {
            if ( PortletTypeHome.findByPrimaryKey( portletType.getId( ) ).getId( ) == null )
            {
                PortletTypeHome.create( portletType );
            }
        }

        _portlet = new TagCloudPortlet( );
        _portlet.setIdCloud( SAMPLE_CLOUD_ID );
        _portlet.setPageId( PortalService.getRootPageId( ) );
        _portlet.setStyleId( 0 );
        _portlet.setColumn( 1 );
        _portlet.setOrder( 1 );
        _portlet.setName( PORTLET_NAME );
        _portlet.setStatus( Portlet.STATUS_PUBLISHED );
        _portlet.setDisplayPortletTitle( 0 );
        _portlet.setDeviceDisplayFlags( Portlet.FLAG_DISPLAY_ON_NORMAL_DEVICE | Portlet.FLAG_DISPLAY_ON_LARGE_DEVICE | Portlet.FLAG_DISPLAY_ON_XLARGE_DEVICE );
        TagCloudPortletHome.getInstance( ).create( _portlet );
    }

    @AfterEach
    @Override
    protected void tearDown( ) throws Exception
    {
        if ( _portlet != null )
        {
            TagCloudPortletHome.getInstance( ).remove( _portlet );
        }

        LocalVariables.remove( );
        super.tearDown( );
    }

    /**
     * The shipped templates are registered in the core for the tag cloud portlet type
     */
    @Test
    public void testShippedTemplatesRegistered( )
    {
        List<PortletTemplate> listTemplates = PortletTemplateHome.findByPortletType( TagCloudPortletHome.getInstance( ).getPortletTypeId( ) );

        assertEquals( SHIPPED_TEMPLATES_COUNT, listTemplates.size( ), "the shipped templates should be registered in the core for the tag cloud portlet type" );
    }

    /**
     * The template chosen for a portlet is stored by the core
     */
    @Test
    public void testTemplateStoredWithThePortlet( )
    {
        PortletTemplate template = PortletTemplateHome.findByPortletType( TagCloudPortletHome.getInstance( ).getPortletTypeId( ) ).get( 1 );
        _portlet.setIdTemplate( template.getId( ) );
        _portlet.update( );

        Portlet stored = PortletHome.findByPrimaryKey( _portlet.getId( ) );

        assertEquals( template.getId( ), stored.getIdTemplate( ), "the chosen template should be stored by the core with the portlet" );
        assertTrue( PortletTemplateHome.isTemplateUsed( template.getId( ) ), "a template chosen by a portlet is used" );
    }

    /**
     * Every shipped template renders the wrapper, the title, the tags and the device display classes
     */
    @Test
    public void testRenderEveryShippedTemplate( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        LocalVariables.setLocal( null, request, new MockHttpServletResponse( ) );

        for ( PortletTemplate template : PortletTemplateHome.findByPortletType( TagCloudPortletHome.getInstance( ).getPortletTypeId( ) ) )
        {
            _portlet.setIdTemplate( template.getId( ) );
            String strContent = _portlet.getHtmlContent( request );
            String strTemplate = "template " + template.getTemplatePath( );

            assertTrue( strContent.contains( MARKER_PORTLET ), strTemplate + " should render the portlet wrapper" );
            assertTrue( strContent.contains( "portlet_" + _portlet.getId( ) ), strTemplate + " should render the portlet id" );
            assertTrue( strContent.contains( PORTLET_NAME ), strTemplate + " should render the portlet title" );
            assertTrue( strContent.contains( SAMPLE_TAG_NAME ), strTemplate + " should render the tag name" );
            assertTrue( strContent.contains( SAMPLE_TAG_URL ), strTemplate + " should link to the tag URL" );
            assertTrue( strContent.contains( SAMPLE_TAG_CLASS ), strTemplate + " should render the tag weight class" );
            assertTrue( strContent.contains( "d-none d-md-block" ), strTemplate + " should hide the portlet on small devices" );
        }
    }

    /**
     * An unknown template falls back to the default one and a hidden title is not rendered
     */
    @Test
    public void testFallbackToDefaultTemplate( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        LocalVariables.setLocal( null, request, new MockHttpServletResponse( ) );

        _portlet.setIdTemplate( UNKNOWN_TEMPLATE_ID );
        _portlet.setDisplayPortletTitle( 1 );
        String strContent = _portlet.getHtmlContent( request );

        assertTrue( strContent.contains( MARKER_PORTLET ), "the default template should render the portlet wrapper" );
        assertTrue( strContent.contains( SAMPLE_TAG_NAME ), "the default template should render the tags" );
        assertFalse( strContent.contains( PORTLET_NAME ), "a hidden portlet title should not be rendered" );
    }
}
