/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.magnum.mobilecloud.video;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VideoController {
	
	/**
	 * You will need to create one or more Spring controllers to fulfill the
	 * requirements of the assignment. If you use this file, please rename it
	 * to something other than "AnEmptyController"
	 * 
	 * 
		 ________  ________  ________  ________          ___       ___  ___  ________  ___  __       
		|\   ____\|\   __  \|\   __  \|\   ___ \        |\  \     |\  \|\  \|\   ____\|\  \|\  \     
		\ \  \___|\ \  \|\  \ \  \|\  \ \  \_|\ \       \ \  \    \ \  \\\  \ \  \___|\ \  \/  /|_   
		 \ \  \  __\ \  \\\  \ \  \\\  \ \  \ \\ \       \ \  \    \ \  \\\  \ \  \    \ \   ___  \  
		  \ \  \|\  \ \  \\\  \ \  \\\  \ \  \_\\ \       \ \  \____\ \  \\\  \ \  \____\ \  \\ \  \ 
		   \ \_______\ \_______\ \_______\ \_______\       \ \_______\ \_______\ \_______\ \__\\ \__\
		    \|_______|\|_______|\|_______|\|_______|        \|_______|\|_______|\|_______|\|__| \|__|
                                                                                                                                                                                                                                                                        
	 * 
	 */
	@Autowired
	VideoRepository videoRepo;
	
	@RequestMapping(value="/video", method=RequestMethod.GET)
	public @ResponseBody Collection<Video> getAllVideos(){
		Collection<Video> res = new ArrayList<Video>();
		Iterable<Video> r = videoRepo.findAll();
		for(Video v:r) {
			res.add(v);
		}
		return res;
	}
	
	@RequestMapping(value="/video",method=RequestMethod.POST)
	public @ResponseBody Video saveVideo(@RequestBody Video vid, HttpServletResponse response) {
		Video res = videoRepo.save(vid);
		return res;
	}
	
	
	@RequestMapping(value="/video/{id}",method=RequestMethod.GET)
	public @ResponseBody Video getVideo(@PathVariable("id") long id, HttpServletResponse response) {
		Video res = videoRepo.findById(id);
		if(res == null) {
			response.setStatus(404);
		}
		return res;
	}
	
	@RequestMapping(value="/video/{id}/like", method=RequestMethod.POST)
	public void likeVideo(@RequestHeader(value="Authorization") String token, HttpServletResponse response, @PathVariable("id") long id) {
		Video res = videoRepo.findById(id);
		if(res == null) {
			response.setStatus(404);
			return;
		}
		if(res.getLikedBy().contains(token)) {
			response.setStatus(400);
			return;
		} else {
			res.setLikes(res.getLikes()+1);
			res.getLikedBy().add(token); 
			videoRepo.save(res);
			response.setStatus(200);
		}
		return;
	}
	
	@RequestMapping(value="/video/{id}/unlike", method=RequestMethod.POST)
	public void unlikeVideo(@RequestHeader(value="Authorization") String token, HttpServletResponse response, @PathVariable("id") long id) {
		Video res = videoRepo.findById(id);
		if(res == null) {
			response.setStatus(404);
			return;
		}
		if(!res.getLikedBy().contains(token)) {
			response.setStatus(400);
			return;
		} else {
			res.setLikes(res.getLikes()-1);
			res.getLikedBy().remove(token); 
			videoRepo.save(res);
			response.setStatus(200);
		}
		return;
	}
	@RequestMapping(value="/video/{id}/likeby", method=RequestMethod.GET)
	public List<String> likebyVideo(HttpServletResponse response, @PathVariable("id") long id) {
		Video res = videoRepo.findById(id);
		List<String> l = new ArrayList<String>();
		if(res == null) {
			response.setStatus(404);
			return l;
		}
		for(String v: res.getLikedBy()) {
			l.add(v);
		}
		return l;
	}
	
	@RequestMapping(value="/video/search/findByName", method = RequestMethod.GET)
	public @ResponseBody List<Video> findByName(@RequestParam("title") String name) {
		return videoRepo.findByName(name);
	}
	
	
	@RequestMapping(value="/video/search/findByDurationLessThan", method = RequestMethod.GET)
	public @ResponseBody List<Video> findByDuration(@RequestParam("duration") long duration) {
		return videoRepo.findByDurationLessThan(duration);
	}
	
	
	
}
