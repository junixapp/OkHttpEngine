# OkHttpEngine
A simple wrapper for OkHttp!

# Feature
- fluent api
- get/post request
- multipart body

# Usage
- get请求

		 OkHttpEngine.create()
                    .get("url", new OkHttpEngine.OkHttpCallback() {
                        @Override
                        public void onSuccess(String result) {
                            
                        }
                        @Override
                        public void onFail(IOException e) {

                        }
                    });

- post请求

		PostParams params = new PostParams();
        params.add("key","value");
        params.add("file",new File("a.jpg"));
        OkHttpEngine.create()
                   .post("url", params, new OkHttpEngine.OkHttpCallback() {
                       @Override
                       public void onSuccess(String result) {

                       }

                       @Override
                       public void onFail(IOException e) {

                       }
                   });

# Depedency

	