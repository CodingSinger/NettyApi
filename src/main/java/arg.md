
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
  
