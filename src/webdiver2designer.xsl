<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:output method="xml" indent="yes" />
   
   <xsl:key name="first-term" match="//term" use="@name" />
   
   <xsl:template match="/webdiver">
      <xsl:element name="designer">
         <xsl:element name="rectangle_set">
            <xsl:apply-templates select="//term" />
         </xsl:element>

         <xsl:element name="association_set">
            <xsl:apply-templates select="//term[normalize-space(parent::term/@name)]" mode="association" />
         </xsl:element>
      </xsl:element>
   </xsl:template>

   <xsl:template match="//term">
     <!-- if statement zorgt voor ontdubbelen -->
     <xsl:if test="count(preceding::term[@name=current()/@name]|ancestor-or-self::term[@name=current()/@name])=1">
      <xsl:element name="rectangle">
         <xsl:copy-of select="@name" />

         <xsl:element name="xposition">
            <xsl:number value="30*position()" />
         </xsl:element>

         <xsl:element name="yposition">
            <xsl:number value="30*position()" />
         </xsl:element>

         <width>50</width>

         <height>30</height>
         
         <xsl:element name="check-double">
           <xsl:value-of select="count(preceding::term[@name=current()/@name]|ancestor-or-self::term[@name=current()/@name])" />
         </xsl:element>

      </xsl:element>
     </xsl:if>
   </xsl:template>

   <xsl:template match="//term[normalize-space(parent::term/@name)]" mode="association">
      <xsl:element name="association">
         <xsl:attribute name="fromClass">
            <xsl:value-of select="@name" />
         </xsl:attribute>

         <xsl:attribute name="toClass">
            <xsl:value-of select="parent::term/@name" />
         </xsl:attribute>

         <xsl:attribute name="type">2</xsl:attribute>

         <point_set>
            <point>
               <position1>
                  <xsl:value-of select="count(preceding::term|ancestor-or-self::term)" />
               </position1>
               
               <xsl:variable name="id1" select="generate-id(key('first-term', @name))" />
               <xsl:for-each select="//term" >
               
                 <xsl:if test="generate-id(.)=$id1">

               <xsl:element name="x">
                  <!--xsl:number value="15*(count(preceding::term|ancestor-or-self::term))+10" /-->
                    <xsl:number value="position()*30" format="1" />
               </xsl:element>

               <xsl:element name="y">
                  <!--xsl:number value="15*(count(preceding::term|ancestor-or-self::term))" /-->
                    <xsl:number value="position()*30" format="1" />
               </xsl:element>
               
                 </xsl:if>
               
               </xsl:for-each>
            </point>

            <point>
                <!--xsl:variable name="id" select="generate-id(parent::term)" /-->
                <!-- In ontdubbelde toestand (TODO): zoek het eerste element in het document met dezelfde naam (@name)
                     als parent::term, en neem daar de position van -->
                <xsl:variable name="id" select="generate-id(key('first-term', parent::term/@name))" />
                <xsl:for-each select="//term" >
                    <xsl:if test="generate-id(.)=$id">
                      <!--xsl:value-of select="position()"/-->
                 <xsl:element name="x">
                    <xsl:number value="position()*30" format="1" />
                 </xsl:element>

                 <xsl:element name="y">
                    <xsl:number value="position()*30" format="1" />
                 </xsl:element>
                    </xsl:if>
               </xsl:for-each>
            </point>
         </point_set>
      </xsl:element>
   </xsl:template>

</xsl:stylesheet>

