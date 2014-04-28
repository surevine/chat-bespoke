/*
 * Copyright (C) 2010 Surevine Limited
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see {http://www.gnu.org/licenses/}.
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
