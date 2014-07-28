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
package fr.paris.lutece.plugins.subscribe.web;

import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionDTO;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.ISubscriptionProviderService;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Subscribe application
 */
@Controller( xpageName = "subscribe", pageTitleI18nKey = "subscribe.viewSubscription.title", pagePathI18nKey = "subscribe.viewSubscription.title" )
public class SubscribeApp extends MVCApplication
{
    private static final long serialVersionUID = 4082230643317543885L;

    private static final String JSP_URL_SUBSCRIBE_XPAGE = "jsp/site/Portal.jsp?page=subscribe";

    private static final String MESSAGE_CONFIRM_REMOVE_SUBSCRIPTION = "module.subscribe.mydashboard.message.confirmRemoveSubscription";
    private static final String MESSAGE_ACCESS_DENIED = "module.subscribe.mydashboard.message.accessDenied";

    private static final String PARAMETER_REFERER = "referer";
    private static final String PARAMETER_ID_SUBSCRIPTION = "idSubscription";
    private static final String PARAMETER_FROM_URL = "from_url";

    private static final String ACTION_DO_REMOVE_URL = "doRemoveSubscription";
    private static final String ACTION_CONFIRM_REMOVE_URL = "confirmRemoveSubscription";

    private static final String VIEW_SUBSCRIPTION_LIST = "viewSubscriptionList";

    private static final String PATH_PORTAL = "jsp/site/";

    private static final String TEMPLATE_MANAGE_SUBSCRIPTION = "skin/plugins/subscribe/view_subscription_list.html";
    private static final String MARK_LIST_SUBSCRIPTION_DTO = "list_subscription_dto";

    /**
     * View the list of subscriptions of a user
     * @param request The request
     * @return The XPage to display
     */
    @View( value = VIEW_SUBSCRIPTION_LIST, defaultView = true )
    public XPage getViewSubscriptionList( HttpServletRequest request )
    {
        XPage xpage = getXPage( );
        xpage.setContent( getSubscriptionList( request ) );
        return xpage;
    }

    /**
     * Get the HTML content to display the list of subscriptions of the current
     * user
     * @param request The user
     * @return The HTML content to display, or an empty string if the user has
     *         not logged in or if the authentication is not enabled
     */
    public static String getSubscriptionList( HttpServletRequest request )
    {
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
            if ( user != null )
            {
                SubscriptionService subscriptionService = SubscriptionService.getInstance( );
                SubscriptionFilter filter = new SubscriptionFilter( );
                filter.setIdSubscriber( user.getName( ) );
                List<Subscription> listSubscription = subscriptionService.findByFilter( filter );
                List<SubscriptionDTO> listSubscriptionDto = new ArrayList<SubscriptionDTO>( listSubscription.size( ) );
                for ( Subscription subscription : listSubscription )
                {
                    ISubscriptionProviderService providerService = subscriptionService.getProviderService( subscription
                            .getSubscriptionProvider( ) );
                    SubscriptionDTO subscriptionDTO = new SubscriptionDTO( );
                    subscriptionDTO.setIdSubscription( subscription.getIdSubscription( ) );
                    subscriptionDTO.setRemovable( providerService.isSubscriptionRemovable( user,
                            subscription.getSubscriptionKey( ), subscription.getIdSubscribedResource( ) ) );
                    subscriptionDTO.setUrlModify( providerService.getUrlModifySubscription( user,
                            subscription.getSubscriptionKey( ), subscription.getIdSubscribedResource( ) ) );
                    subscriptionDTO.setHtmlSubscription( providerService.getSubscriptionHtmlDescription( user,
                            subscription.getSubscriptionKey( ), subscription.getIdSubscribedResource( ),
                            request.getLocale( ) ) );
                    listSubscriptionDto.add( subscriptionDTO );
                }

                Map<String, Object> model = new HashMap<String, Object>( );
                model.put( MARK_LIST_SUBSCRIPTION_DTO, listSubscriptionDto );

                HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_SUBSCRIPTION,
                        request.getLocale( ), model );

                return template.getHtml( );
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * Get the confirmation message before removing a subscription
     * @param request The request
     * @return a XPage
     * @throws SiteMessageException A SiteMessageException to display the
     *             confirmation message
     */
    @Action( ACTION_CONFIRM_REMOVE_URL )
    public XPage confirmRemoveSubscription( HttpServletRequest request ) throws SiteMessageException
    {
        String strReferer = request.getHeader( PARAMETER_REFERER );
        UrlItem urlItem = new UrlItem( PATH_PORTAL + getActionUrl( ACTION_DO_REMOVE_URL ) );
        urlItem.addParameter( PARAMETER_ID_SUBSCRIPTION, request.getParameter( PARAMETER_ID_SUBSCRIPTION ) );

        if ( StringUtils.isNotEmpty( strReferer ) )
        {
            try
            {
                urlItem.addParameter( PARAMETER_FROM_URL, URIUtil.encodeWithinQuery( strReferer ) );
            }
            catch ( URIException e )
            {
                AppLogService.error( e.getMessage( ), e );
            }
        }
        SiteMessageService.setMessage( request, MESSAGE_CONFIRM_REMOVE_SUBSCRIPTION, SiteMessage.TYPE_CONFIRMATION,
                urlItem.getUrl( ) );

        return null;
    }

    /**
     * Do remove a subscription
     * @param request The request
     * @return a XPage
     * @throws SiteMessageException If the user is not allow to modify the
     *             subscription
     */
    @Action( ACTION_DO_REMOVE_URL )
    public XPage doRemoveSubscription( HttpServletRequest request ) throws SiteMessageException
    {
        String strIdSubscription = request.getParameter( PARAMETER_ID_SUBSCRIPTION );

        if ( StringUtils.isNotEmpty( strIdSubscription ) && StringUtils.isNumeric( strIdSubscription ) )
        {
            int nIdSubscription = Integer.parseInt( strIdSubscription );
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
            Subscription subscription = SubscriptionService.getInstance( ).findBySubscriptionId( nIdSubscription );
            if ( user != null && subscription != null
                    && StringUtils.equals( subscription.getUserId( ), user.getName( ) ) )
            {
                SubscriptionService.getInstance( ).removeSubscription( nIdSubscription, true );
            }
            else
            {
                SiteMessageService.setMessage( request, MESSAGE_ACCESS_DENIED, SiteMessage.TYPE_STOP );
            }
        }

        String strReferer = request.getParameter( PARAMETER_FROM_URL );
        String strUrl;
        if ( StringUtils.isNotEmpty( strReferer ) )
        {
            strUrl = strReferer;
        }
        else
        {
            strUrl = AppPathService.getBaseUrl( request ) + JSP_URL_SUBSCRIBE_XPAGE;
        }

        redirect( request, strUrl );
        return new XPage( );
    }
}