/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.subscribe.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * This class provides Data Access methods for Subscription objects
 */

public final class SubscriptionDAO implements ISubscriptionDAO
{

    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_subscription ) FROM subscribe_subscription";
    private static final String SQL_QUERY_SELECT = " SELECT id_subscription, id_user, subscription_provider, subscription_key, id_subscribed_resource FROM subscribe_subscription ";
    private static final String SQL_QUERY_SELECT_FROM_SUBSCRIPTION_ID = SQL_QUERY_SELECT + " WHERE id_subscription = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO subscribe_subscription ( id_subscription, id_user, subscription_provider, subscription_key, id_subscribed_resource ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM subscribe_subscription WHERE id_subscription = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE subscribe_subscription SET id_user = ?, subscription_provider = ?, subscription_key = ?, id_subscribed_resource = ? WHERE id_subscription = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_subscription, id_user, subscription_provider, subscription_key, id_subscribed_resource FROM subscribe_subscription";

    private static final String SQL_FILTER_ID_USER = " id_user = ? ";
    private static final String SQL_FILTER_PROVIDER = " subscription_provider = ? ";
    private static final String SQL_FILTER_SUBSCRIPTION_KEY = " subscription_key = ? ";
    private static final String SQL_FILTER_ID_SUBSCRIBED_RESOURCE = " id_subscribed_resource = ? ";
    private static final String CONSTANT_WHERE = " WHERE ";
    private static final String CONSTANT_AND = " AND ";

    /**
     * Get a new primary key
     * 
     * @param plugin
     *            The plugin
     * @return The new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin ) )
        {
            daoUtil.executeQuery( );

            int nKey = 1;
            if ( daoUtil.next( ) )
            {
                nKey = daoUtil.getInt( 1 ) + 1;
            }

            daoUtil.free( );
            return nKey;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( Subscription subscription, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {

            subscription.setIdSubscription( newPrimaryKey( plugin ) );

            daoUtil.setInt( 1, subscription.getIdSubscription( ) );
            daoUtil.setString( 2, subscription.getUserId( ) );
            daoUtil.setString( 3, subscription.getSubscriptionProvider( ) );
            daoUtil.setString( 4, subscription.getSubscriptionKey( ) );
            daoUtil.setString( 5, subscription.getIdSubscribedResource( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Subscription load( int nId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_FROM_SUBSCRIPTION_ID, plugin ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            Subscription subscription = null;

            if ( daoUtil.next( ) )
            {
                subscription = new Subscription( );
                subscription.setIdSubscription( daoUtil.getInt( 1 ) );
                subscription.setUserId( daoUtil.getString( 2 ) );
                subscription.setSubscriptionProvider( daoUtil.getString( 3 ) );
                subscription.setSubscriptionKey( daoUtil.getString( 4 ) );
                subscription.setIdSubscribedResource( daoUtil.getString( 5 ) );
            }

            daoUtil.free( );
            return subscription;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nSubscriptionId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nSubscriptionId );
            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Subscription subscription, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {

            daoUtil.setString( 1, subscription.getUserId( ) );
            daoUtil.setString( 2, subscription.getSubscriptionProvider( ) );
            daoUtil.setString( 3, subscription.getSubscriptionKey( ) );
            daoUtil.setString( 4, subscription.getIdSubscribedResource( ) );
            daoUtil.setInt( 5, subscription.getIdSubscription( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Subscription> selectSubscriptionsList( Plugin plugin )
    {
        Collection<Subscription> subscriptionList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Subscription subscription = new Subscription( );

                subscription.setIdSubscription( daoUtil.getInt( 1 ) );
                subscription.setUserId( daoUtil.getString( 2 ) );
                subscription.setSubscriptionProvider( daoUtil.getString( 3 ) );
                subscription.setSubscriptionKey( daoUtil.getString( 4 ) );
                subscription.setIdSubscribedResource( daoUtil.getString( 5 ) );

                subscriptionList.add( subscription );
            }

            daoUtil.free( );
        }
        return subscriptionList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Subscription> findByFilter( SubscriptionFilter filter )
    {
        List<Subscription> listSubscription = new ArrayList<>( );
        boolean bHasFilter = false;
        StringBuilder sbSql = new StringBuilder( SQL_QUERY_SELECT );
        if ( StringUtils.isNotEmpty( filter.getUserId( ) ) )
        {
            sbSql.append( CONSTANT_WHERE );
            sbSql.append( SQL_FILTER_ID_USER );
            bHasFilter = true;
        }
        if ( filter.getSubscriptionProvider( ) != null )
        {
            if ( bHasFilter )
            {
                sbSql.append( CONSTANT_AND );
            }
            else
            {
                sbSql.append( CONSTANT_WHERE );
                bHasFilter = true;
            }
            sbSql.append( SQL_FILTER_PROVIDER );
        }
        if ( filter.getSubscriptionKey( ) != null )
        {
            if ( bHasFilter )
            {
                sbSql.append( CONSTANT_AND );
            }
            else
            {
                sbSql.append( CONSTANT_WHERE );
                bHasFilter = true;
            }
            sbSql.append( SQL_FILTER_SUBSCRIPTION_KEY );
        }
        if ( filter.getIdSubscribedResource( ) != null )
        {
            if ( bHasFilter )
            {
                sbSql.append( CONSTANT_AND );
            }
            else
            {
                sbSql.append( CONSTANT_WHERE );
                bHasFilter = true;
            }
            sbSql.append( SQL_FILTER_ID_SUBSCRIBED_RESOURCE );
        }

        int nIndex = 1;
        try ( DAOUtil daoUtil = new DAOUtil( sbSql.toString( ) ) )
        {
            if ( StringUtils.isNotEmpty( filter.getUserId( ) ) )
            {
                daoUtil.setString( nIndex++, filter.getUserId( ) );
            }
            if ( filter.getSubscriptionProvider( ) != null )
            {
                daoUtil.setString( nIndex++, filter.getSubscriptionProvider( ) );
            }
            if ( filter.getSubscriptionKey( ) != null )
            {
                daoUtil.setString( nIndex++, filter.getSubscriptionKey( ) );
            }
            if ( filter.getIdSubscribedResource( ) != null )
            {
                // Warning, no increment here !
                daoUtil.setString( nIndex, filter.getIdSubscribedResource( ) );
            }
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Subscription subscription = new Subscription( );
                subscription.setIdSubscription( daoUtil.getInt( 1 ) );
                subscription.setUserId( daoUtil.getString( 2 ) );
                subscription.setSubscriptionProvider( daoUtil.getString( 3 ) );
                subscription.setSubscriptionKey( daoUtil.getString( 4 ) );
                subscription.setIdSubscribedResource( daoUtil.getString( 5 ) );
                listSubscription.add( subscription );
            }

            daoUtil.free( );
        }

        return listSubscription;
    }
}
