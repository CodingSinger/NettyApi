
转换器 
类型转换 将字符串转化为任意类型的类
Conversion:
- 注册Converter
- 主要是负责查找适合的Converter并进行转换

解析器 解析出来参数对象，根据实际类型进行类型转化并进行字段注入，是入口
ArgumentResolver：
- 提取MethodParameter和请求中对应的参数进行注入或者赋值
    

绑定器 :主要是将值进行注入或者简单的赋值
- 如SimpleRequestDataBinder只负责简单类型的转换
- ModelAttributeDataBinder则负责对Model对象的注入
    doBinder:是对每一个MethodParameter调用一次，解析出所有请求中的key-value形式，然后从该
    Bean中寻找匹配的key,注入
    伪代码：
    def doBind(model):
        for k,v in requestMaps:
            property = model[property]
            set(property,v)
             
如果是多层Bean的嵌套格式 也采用SpringMVC中的`p1.p2.p3 = value` 格式来表示某一个Bean的嵌套关系注入
注入方法的需要加一个递归实现：
即首先获取(生成)Bean中p1属性的对象，然后再用该对象Bean去获取(生成)p2属性对象，直到最后一层调用实际的setProperty方法
进行注入。

和SpringMVC一样，注入值也需要属性提供set方法，并且对于嵌套的注入，还需要中间Bean提供set、get方法


```java
	@Override
	public void setPropertyValue(PropertyValue pv) throws BeansException {
		PropertyTokenHolder tokens = (PropertyTokenHolder) pv.resolvedTokens;
		if (tokens == null) {
			String propertyName = pv.getName();
			AbstractNestablePropertyAccessor nestedPa;
			try {
                //获取实际要注入的对象的包装,例如p1.p2.p3实际要注入的为p3的包装
				nestedPa = getPropertyAccessorForPropertyPath(propertyName);
			}
			catch (NotReadablePropertyException ex) {
				throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName,
						"Nested property in path '" + propertyName + "' does not exist", ex);
			}
			tokens = getPropertyNameTokens(getFinalPath(nestedPa, propertyName));
			if (nestedPa == this) {
				pv.getOriginalPropertyValue().resolvedTokens = tokens;
			}
			nestedPa.setPropertyValue(tokens, pv); //实际的执行属性注入方法
		}
		else {
			setPropertyValue(tokens, pv); //实际的执行属性注入方法
		}
	}

```
  
AbstractNestablePropertyAccessor#getPropertyAccessorForPropertyPath方法如下：

```java

	protected AbstractNestablePropertyAccessor getPropertyAccessorForPropertyPath(String propertyPath) {
		int pos = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(propertyPath);
		// Handle nested properties recursively.
		if (pos > -1) {
			String nestedProperty = propertyPath.substring(0, pos);//当前nestedPa中需要获取的属性名称
			String nestedPath = propertyPath.substring(pos + 1); //获取下一层的属性名称
			AbstractNestablePropertyAccessor nestedPa = getNestedPropertyAccessor(nestedProperty); //获取该属性名称在当前nestedPa对象中的包装nestedPa对象,如在p1中获得p2的nestedPa
			return nestedPa.getPropertyAccessorForPropertyPath(nestedPath);
		}
		else {
			return this; //已经到最底层了 返回该对象包装
		
```
