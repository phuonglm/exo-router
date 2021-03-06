package org.exoplatform.ks.router;

import junit.framework.TestCase;

import org.exoplatform.ks.router.ExoRouter;
import org.exoplatform.ks.router.ExoRouter.Route;
import org.exoplatform.ks.router.event.BaseEvent;
import org.exoplatform.ks.router.event.HandleException;
import org.exoplatform.ks.router.impl.handler.BaseEventHandler;
import org.exoplatform.ks.router.impl.handler.ForumEvent;
import org.exoplatform.ks.router.impl.handler.HandlerFactory;

/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 23, 2012  
 */
public class ExoRouterHandlerTest extends TestCase {
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    initRouter();
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    ExoRouter.reset();
  }
  
  private void initRouter() {
    //{selectedNode}/ForumService
    ExoRouter.prependRoute("/{pageID}/ForumService", "forum.home");
    //{selectedNode}/SearchForum
    ExoRouter.addRoute("/{pageID}/SearchForum", "forum.search");
    //{selectedNode}Tag
    ExoRouter.addRoute("/{pageID}/Tag", "forum.tag");
    //{selectedNode}/topic/{topicId}
    ExoRouter.addRoute("/{pageID}/topic/{topicID}", "forum.topic.show");
    //{selectedNode}/topic/topic{topicId}/true
    ExoRouter.addRoute("/{pageID}/topic/{topicID}/reply", "forum.topic.reply");
    //{selectedNode}/topic/{topicId}/false
    ExoRouter.addRoute("/{pageID}/topic/{topicID}/quote", "forum.topic.quote");
    //{selectedNode}/topic/{topicID}/{postID}
    ExoRouter.addRoute("/{pageID}/topic/{topicID}/post/{postID}", "forum.topic.post.show");
    //{selectedNode}/topic/topic{topicID}/
    ExoRouter.addRoute("/{pageID}/topic/{topicID}/page/{pageNo}", "forum.topic.page");
    //{selectedNode}/forum/forum{forumID}
    ExoRouter.addRoute("/{pageID}/forum/{forumID}", "forum.show");
    //{selectNode}/forum/{forumID}/{number}
    ExoRouter.addRoute("/{pageID}/forum/{forumID}/page/{pageNo}", "forum.show.page");
    
    //{selectNode}/category/{categoryID}
    ExoRouter.addRoute("/{pageID}/category/{categoryID}", "category.show");
  }
  
  private void doExecute(BaseEvent event) throws HandleException {
    //
    BaseEventHandler handler = HandlerFactory.getHandler(event);
    assertNotNull(handler);
    handler.handle(event);
  }
  
  public void testRouterHandlerForForumHome() throws Exception {
    Route route = ExoRouter.route("/12345/ForumService");
    assertNotNull(route);
    assertEquals("forum.home", route.action);
    assertEquals("12345", route.localArgs.get("pageID"));
    BaseEvent event = new ForumEvent(this, route.action, route.localArgs);
    doExecute(event);
    
  }
  
  public void testRouterForSearch() throws Exception {
    Route route = ExoRouter.route("/12345/SearchForum");
    assertNotNull(route);
    assertEquals("forum.search", route.action);
    assertEquals("12345", route.localArgs.get("pageID"));
    
    BaseEvent event = new ForumEvent(this, route.action, route.localArgs);
    doExecute(event);
    
  }
  
  public void testRouterForTag() throws Exception {
    Route route = ExoRouter.route("/12345/Tag");
    assertNotNull(route);
    assertEquals("forum.tag", route.action);
    assertEquals("12345", route.localArgs.get("pageID"));
    BaseEvent event = new ForumEvent(this, route.action, route.localArgs);
    doExecute(event);
  }
  /**
   * Handler =>OK
   * @throws Exception
   */
  public void testRouterForShowTopic() throws Exception {
    Route route = ExoRouter.route("/12345/topic/topic987654321");
    assertNotNull(route);
    assertEquals("forum.topic.show", route.action);
    assertEquals("12345", route.localArgs.get("pageID"));
    assertEquals("topic987654321", route.localArgs.get("topicID"));
    
    BaseEvent event = new ForumEvent(this, route.action, route.localArgs);
    doExecute(event);
  }
  /**
   * Handler =>OK
   * @throws Exception
   */
  public void testRouterForShowTopicReply() throws Exception {
    //ExoRouter.addRoute("/{pageID}/topic/{topicID}/{reply}", "forum.tag.show.true");
    Route route = ExoRouter.route("/12345/topic/topic987654321/reply");
    assertNotNull(route);
    assertEquals("forum.topic.reply", route.action);
    assertEquals("12345", route.localArgs.get("pageID"));
    assertEquals("topic987654321", route.localArgs.get("topicID"));
    
    //
    BaseEvent event = new ForumEvent(this, route.action, route.localArgs);
    doExecute(event);
  }
  
  /**
   * Handler =>OK
   * @throws Exception
   */
  public void testRouterForShowTopicQuote() throws Exception {
    //ExoRouter.addRoute("/{pageID}/topic/{topicID}/{reply}", "forum.tag.show.true");
    Route route = ExoRouter.route("/12345/topic/topic987654321/quote");
    assertNotNull(route);
    assertEquals("forum.topic.quote", route.action);
    assertEquals("12345", route.localArgs.get("pageID"));
    assertEquals("topic987654321", route.localArgs.get("topicID"));
    //
    BaseEvent event = new ForumEvent(this, route.action, route.localArgs);
    doExecute(event);
  }
  
  public void testRouterForShowTopicWrong() throws Exception {
    //ExoRouter.addRoute("/{pageID}/topic/{topicID}/{reply}", "forum.tag.show.true");
    Route route = ExoRouter.route("/12345/fail/topic987654321");
    assertNull(route);
  }
  /**
   * Handler =>OK
   * @throws Exception
   */
  public void testRouterForShowTopicPage() throws Exception {
    //ExoRouter.addRoute("/{pageID}/topic/{topicID}/{[0-9]}", "forum.topic.page");
    Route route = ExoRouter.route("/12345/topic/topic987654321/page/3");
    assertNotNull(route);
    assertEquals("forum.topic.page", route.action);
    assertEquals("12345", route.localArgs.get("pageID"));
    assertEquals("topic987654321", route.localArgs.get("topicID"));
    assertEquals("3", route.localArgs.get("pageNo"));
    
    BaseEvent event = new ForumEvent(this, route.action, route.localArgs);
    doExecute(event);
  }
  
  /**
   * Handler =>OK
   * @throws Exception
   */
  public void testRouterForTopicPostShow() throws Exception {
    //ExoRouter.addRoute("/{pageID}/topic/{topicID}/post/{postID}", "forum.topic.post.show");
    Route route = ExoRouter.route("/12345/topic/topic987654321/post/post987654321");
    assertNotNull(route);
    assertEquals("forum.topic.post.show", route.action);
    assertEquals("12345", route.localArgs.get("pageID"));
    assertEquals("topic987654321", route.localArgs.get("topicID"));
    assertEquals("post987654321", route.localArgs.get("postID"));
    //
    BaseEvent event = new ForumEvent(this, route.action, route.localArgs);
    doExecute(event);
  }
  
  public void testRouterForCategoryShow() throws Exception {
    //ExoRouter.addRoute("/{pageID}/category/{categoryID}", "category.show");
    Route route = ExoRouter.route("/12345/category/category987654321");
    assertNotNull(route);
    assertEquals("category.show", route.action);
    assertEquals("12345", route.localArgs.get("pageID"));
    assertEquals("category987654321", route.localArgs.get("categoryID"));
    //
    BaseEvent event = new ForumEvent(this, route.action, route.localArgs);
    doExecute(event);
  }
}
