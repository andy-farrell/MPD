package com.example.cw_mpd_19;
// Andrew Farrell - S1511335 \\

public class rssFeed {

    private String channelTitle;
    private String channelDesc;
    private String channelLink;
    private String channelLastBuild;
    private String itemTitle;
    private String itemDesc;
    private String itemLink;
    private String itemPubDate;
    private String itemCategory;
    private Double itemLat;
    private Double itemLon;

    public rssFeed() //channel contains general earthquake info
    {
        channelTitle = "";
        channelDesc = "";
        channelLink = "";
        channelLastBuild = "";
        itemTitle = "";
        itemDesc = "";
        itemLink = "";
        itemPubDate = "";
        itemCategory = "";
        itemLat = 0.0;
        itemLon = 0.0;

    }

    public rssFeed(String xchtitle, String xchdesc, String xchlink, String xchbuild,
                   String xitemtitle, String xitemdesc, String xitemlink, String xcategory,
                   Double xLat, Double xLon, String xpubdate)
    {
        channelTitle = xchtitle;
        channelDesc = xchdesc;
        channelLink = xchlink;
        channelLastBuild = xchbuild;
        itemTitle = xitemtitle;
        itemDesc = xitemdesc;
        itemLink = xitemlink;
        itemPubDate = xpubdate;
        itemCategory = xcategory;
        itemLat = xLat;
        itemLon = xLon;

    }

    // Getters and Setters for variables

    // channel title
    public String getChannelTitle()
    {
        return channelTitle;
    }
    public void setChannelTitle(String xchtitle)
    {
        channelTitle = xchtitle;
    }

    // channel description
    public String getChannelDesc()
    {
        return channelDesc;
    }
    public void setChannelDesc(String xchdesc)
    {
        channelDesc = xchdesc;
    }

    // channel link
    public String getChannelLink()
    {
        return channelLink;
    }
    public void setChannelLink(String xchlink)
    {
        channelLink = xchlink;
    }

    // channel last build date
    public String getChannelLastBuild() { return channelLastBuild; }
    public void setChannelLastBuild(String xchbuild) { channelLastBuild = xchbuild; }

    // item title
    public String getItemTitle () { return itemTitle;}
    public void setItemTitle(String xitemtitle) { itemTitle = xitemtitle; }

    // item description
    public String getItemDesc () { return itemDesc; }
    public void setItemDesc(String xitemdesc) {itemDesc = xitemdesc; }

    // item link
    public String getItemLink () { return itemLink; }
    public void setItemLink (String xitemlink) {itemLink = xitemlink;}

    // item publish date
    public String getItemPubDate() { return itemPubDate; }
    public void setItemPubDate(String xpubdate) { itemPubDate = xpubdate; }

    // item category
    public String getItemCategory() { return itemCategory; }
    public void setItemCategory(String xcategory ) { itemCategory = xcategory; }

    // item latitude
    public Double getItemLat() { return itemLat; }
    public void setItemLat(Double xLat) { itemLat = xLat;}

    // item longitude
    public Double getItemLon() { return itemLon; }
    public void setItemLon(Double xLon) { itemLon = xLon; }


    public String toString()
    {
        String out;
        out = "\n \n" + itemTitle + " \n \n" + itemDesc + " \n \n" + itemLink + " \n \n"
                + itemPubDate + "\n \n" + itemCategory + "\n \n" + itemLat + "\n \n"
                + itemLon + "\n \n";
        String separator = System.getProperty("line.separator");
        out = out.replaceAll("<br />", separator);
        return out;
    }

}

