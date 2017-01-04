package com.absurd.kafka.springkafka.web;

import com.absurd.kafka.springkafka.job.ResponseDTO;
import com.absurd.kafka.springkafka.job.ServiceException;
import com.absurd.kafka.springkafka.job.TaskInfo;
import com.absurd.kafka.springkafka.thread.TaskServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:wangwenwei@myhexin.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.kafka.springkafka.web
 * @Description:
 * @date 2017/1/3 13:41
 */
@Controller
@RequestMapping("/jobs")
public class TaskManageController {
    @Autowired
    private TaskServiceImpl taskServiceImpl;

    /**
     * Index.jsp
     * 2016年10月8日下午6:39:15
     */
    @RequestMapping(value={"", "/", "index"})
    public String info(){
        return "index.jsp";
    }

    /**
     * 任务列表
     * @return
     * 2016年10月9日上午11:36:03
     */
    @RequestMapping(value="list", method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map list(){
        Map<String, Object> map = new HashMap<>();
        List<TaskInfo> infos = taskServiceImpl.list();
        map.put("rows", infos);
        map.put("total", infos.size());
        return map;
    }

    /**
     * 保存定时任务
     * @param info
     * 2016年10月9日下午1:36:59
     */
    @ResponseBody
    @RequestMapping(value="save", method=RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public Object save(TaskInfo info){
        try {
            if(info.getId() == 0) {
                taskServiceImpl.addJob(info);
            }else{
                taskServiceImpl.edit(info);
            }
        } catch (ServiceException e) {
            return  new ResponseDTO(-1, e.getMessage());
        }
        return new ResponseDTO(0, "", null);
    }

    /**
     * 删除定时任务
     * @param jobName
     * @param jobGroup
     * 2016年10月9日下午1:52:20
     */
    @ResponseBody
    @RequestMapping(value="delete/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8")
    public Object delete(@PathVariable String jobName, @PathVariable String jobGroup){
        try {
            taskServiceImpl.delete(jobName, jobGroup);
        } catch (ServiceException e) {
            return  new ResponseDTO(-1, e.getMessage());
        }
        return new ResponseDTO(0, "", null);
    }

    /**
     * 暂停定时任务
     * @param jobName
     * @param jobGroup
     * 2016年10月10日上午9:41:25
     */
    @ResponseBody
    @RequestMapping(value="pause/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8")
    public Object pause(@PathVariable String jobName, @PathVariable String jobGroup){
        try {
            taskServiceImpl.pause(jobName, jobGroup);
        } catch (ServiceException e) {
            return  new ResponseDTO(-1, e.getMessage());
        }
        return new ResponseDTO(0, "", null);
    }

    /**
     * 重新开始定时任务
     * @param jobName
     * @param jobGroup
     * 2016年10月10日上午9:41:40
     */
    @ResponseBody
    @RequestMapping(value="resume/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8")
    public Object resume(@PathVariable String jobName, @PathVariable String jobGroup){
        try {
            taskServiceImpl.resume(jobName, jobGroup);
        } catch (ServiceException e) {
            return  new ResponseDTO(-1, e.getMessage());
        }
        return new ResponseDTO(0, "", null);
    }
}
