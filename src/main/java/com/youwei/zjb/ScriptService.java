package com.youwei.zjb;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SimpDaoTool;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

@Module(name="/script")
public class ScriptService {

	private static ScriptEngineManager sm = new ScriptEngineManager();
	
	@WebMethod
	public ModelAndView run(String scripts){
		ModelAndView mv = new ModelAndView();
		ScriptEngine engine = sm.getEngineByName("JavaScript");
		Bindings bindings = prepareBindings(engine);
		bindings.put("mv", mv);
		engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		Object result="";
		try {
			// 执行脚本
			if(scripts.contains("java.io")){
				throw new RuntimeException("java.io is forbiden,user sys.io instead");
			}
			result = engine.eval(scripts);
		}catch (ScriptException e) {
			throw new RuntimeException(e);
		}
		return mv;
	}
	
	
	
	private static Bindings prepareBindings(ScriptEngine engine) {
		Bindings bindings = engine.createBindings();
		bindings.put("dao", SimpDaoTool.getGlobalCommonDaoService());
		bindings.put("engine", engine);
		return bindings;
	}
}
