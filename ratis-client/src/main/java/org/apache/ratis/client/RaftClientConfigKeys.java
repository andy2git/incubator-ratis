/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ratis.client;

import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.util.TimeDuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.apache.ratis.conf.ConfUtils.*;

public interface RaftClientConfigKeys {
  Logger LOG = LoggerFactory.getLogger(RaftClientConfigKeys.class);

  static Consumer<String> getDefaultLog() {
    return LOG::debug;
  }

  String PREFIX = "raft.client";

  interface Rpc {
    String PREFIX = RaftClientConfigKeys.PREFIX + ".rpc";

    String REQUEST_TIMEOUT_KEY = PREFIX + ".request.timeout";
    TimeDuration REQUEST_TIMEOUT_DEFAULT = TimeDuration.valueOf(3000, TimeUnit.MILLISECONDS);
    static TimeDuration requestTimeout(RaftProperties properties) {
      return getTimeDuration(properties.getTimeDuration(REQUEST_TIMEOUT_DEFAULT.getUnit()),
          REQUEST_TIMEOUT_KEY, REQUEST_TIMEOUT_DEFAULT, getDefaultLog());
    }
    static void setRequestTimeout(RaftProperties properties, TimeDuration timeoutDuration) {
      setTimeDuration(properties::setTimeDuration, REQUEST_TIMEOUT_KEY, timeoutDuration);
    }

    String WATCH_REQUEST_TIMEOUT_KEY = PREFIX + ".watch.request.timeout";
    TimeDuration WATCH_REQUEST_TIMEOUT_DEFAULT =
        TimeDuration.valueOf(10000, TimeUnit.MILLISECONDS);
    static TimeDuration watchRequestTimeout(RaftProperties properties) {
      return getTimeDuration(properties.getTimeDuration(WATCH_REQUEST_TIMEOUT_DEFAULT.getUnit()),
          WATCH_REQUEST_TIMEOUT_KEY, WATCH_REQUEST_TIMEOUT_DEFAULT, getDefaultLog());
    }
    static void setWatchRequestTimeout(RaftProperties properties,
        TimeDuration timeoutDuration) {
      setTimeDuration(properties::setTimeDuration, WATCH_REQUEST_TIMEOUT_KEY, timeoutDuration);
    }
  }

  interface Async {
    String PREFIX = RaftClientConfigKeys.PREFIX + ".async";

    String MAX_OUTSTANDING_REQUESTS_KEY = PREFIX + ".outstanding-requests.max";
    int MAX_OUTSTANDING_REQUESTS_DEFAULT = 100;
    static int maxOutstandingRequests(RaftProperties properties) {
      return getInt(properties::getInt, MAX_OUTSTANDING_REQUESTS_KEY,
          MAX_OUTSTANDING_REQUESTS_DEFAULT, getDefaultLog(), requireMin(2));
    }
    static void setMaxOutstandingRequests(RaftProperties properties, int outstandingRequests) {
      setInt(properties::setInt, MAX_OUTSTANDING_REQUESTS_KEY, outstandingRequests);
    }

    interface Experimental {
      String PREFIX = Async.PREFIX + "." + Experimental.class.getSimpleName().toLowerCase();

      String SEND_DUMMY_REQUEST_KEY = PREFIX + ".send-dummy-request";
      boolean SEND_DUMMY_REQUEST_DEFAULT = true;
      static boolean sendDummyRequest(RaftProperties properties) {
        return getBoolean(properties::getBoolean, SEND_DUMMY_REQUEST_KEY, SEND_DUMMY_REQUEST_DEFAULT, getDefaultLog());
      }
      static void setSendDummyRequest(RaftProperties properties, boolean sendDummyRequest) {
        setBoolean(properties::setBoolean, SEND_DUMMY_REQUEST_KEY, sendDummyRequest);
      }
    }
  }

  static void main(String[] args) {
    printAll(RaftClientConfigKeys.class);
  }
}
