<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:param name="site-path" select="site-path" />
    <xsl:variable name="portlet-id" select="portlet/portlet-id" />
    
    <xsl:template match="portlet">
        <div class="portlet-background-colored" >
            <xsl:if test="not(string(display-portlet-title)='1')">
                <h3 class="portlet-background-colored-header">
                    <xsl:value-of disable-output-escaping="yes" select="portlet-name" />
                </h3>
            </xsl:if>
            <div class="portlet-background-colored-content" >
                <xsl:apply-templates select="cloud" />
            </div>
        </div>
    </xsl:template>
    <xsl:template match="cloud">
        <ul id="tagcloud">
            <xsl:apply-templates select="tag" />
        </ul>
    </xsl:template>
    <xsl:template match="tag">
        <li>
            <a>
                <xsl:attribute name="href">
                    <xsl:value-of select="tag-url"/>
                </xsl:attribute>
                <xsl:attribute name="class">
                    tag<xsl:value-of select="tag-weight"/>
                </xsl:attribute>
                <xsl:value-of select="tag-name" /> 
            </a>
        </li>
    </xsl:template>
    
</xsl:stylesheet>

