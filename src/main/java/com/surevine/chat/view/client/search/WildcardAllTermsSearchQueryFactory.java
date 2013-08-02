/*
 * Copyright (C) 2010 Surevine Ltd.
 *
 * All rights reserved.
 */
package com.surevine.chat.view.client.search;

import java.util.HashMap;

import com.calclab.hablar.search.client.SearchQueryFactory;

/**
 * Search query factory which will add wildcards around each of the words in the
 * search query.<br />
 * e.g. the query "this is a test" will be converted to "*this* *is* *a* *test*"
 * and used as the nickname search
 */
public class WildcardAllTermsSearchQueryFactory implements SearchQueryFactory {

    @Override
    public HashMap<String, String> createSearchQuery(String term) {
        HashMap<String, String> result = new HashMap<String, String>();

        result.put("nick", term.replaceAll("(\\S+)", "*$1*"));

        return result;
    }

}
