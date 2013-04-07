package com.work.designer.validators;

import java.util.List;

import org.eclipse.jface.viewers.ICellEditorValidator;

import com.work.designer.model.BaseElement;
import com.work.designer.model.Field;
import com.work.designer.model.Form;
import com.work.designer.model.Process;
import com.work.designer.model.Task;
import com.work.designer.model.Transition;
import com.work.designer.utils.StringUtils;

public class NameUniqueValidator implements ICellEditorValidator 
{
	private BaseElement object = null;
	
	public NameUniqueValidator(BaseElement object)
	{
		this.object = object;
	}

	@Override
	public String isValid(Object value)
	{
		String input = (String)value;
		
		boolean bRet = isModelExist(this.object, input);
		if (bRet) 
		{
			return "ָ�������ֺ����еĿؼ�����";
		}
		String regString = "";
		String errorInfo = "";
		if ((this.object instanceof Field)) 
		{
			regString = "[a-z][a-z][a-zA-Z0-9]*";
			errorInfo = "����ǰ��λ����ΪСд��ĸa-z";
		} 
		else 
		{
			regString = "[a-zA-Z][a-zA-Z0-9][a-zA-Z0-9]*";
			errorInfo = "����ǰ��λ����Ϊ��ĸa-z��A-Z";
		}

		if (!input.matches(regString)) 
		{
			return "ָ�������ֲ��Ϸ���" + errorInfo;
		}

		if (isKeyValue(input)) 
		{
			return "ָ��������Ϊ�����ؼ���";
		}
		return null;
	}
	
	private boolean isKeyValue(String input)
	{
		return StringUtils.isKeyValue(input);
	}

	/**
	 * �ж��Ƿ�����
	 * @param object
	 * @param value
	 * @return
	 */
	private boolean isModelExist(BaseElement object, String value)
	{
		if(object instanceof Field)
		{
			BaseElement element = object.getParent();
			if(element == null) return false;
			if(element instanceof Form)
			{
				List<Field> fields = ((Form)element).getFields();
				for(Field field : fields)
				{
					if(object != field && field.getName().equals(value))
					{
						return true;
					}
				}
			}
			if(element instanceof Task)
			{
				List<Field> fields = ((Task)element).getFields();
				for(Field field : fields)
				{
					if(object != field && field.getName().equals(value))
					{
						return true;
					}
				}
			}
		}
		else if(object instanceof Transition)
		{
			Task task = ((Transition)object).getSource();
			for(Transition t : task.getOutTransitions())
			{
				if(object != t && t.getName().equals(value))
				{
					return true;
				}
			}
		}
		else if(object instanceof Task)
		{
			BaseElement process = ((Task)object).getParent();
			List<Task> tasks = ((Process)process).getTasks();
			for(Task task : tasks)
			{
				if(object != task && task.getName().equals(value))
				{
					return true;
				}
			}
		}
		return false;
	}
}
