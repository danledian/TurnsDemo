# 循环播放广告页
无限循环逻辑部分采用[banner](https://github.com/youth5201314/banner)的思路，非常感谢[youth5201314](https://github.com/youth5201314)的开源。

本项目具体实现将`ViewPager`和`PageIndicator`分离，具体应用场景可根据需求自行布局，`ViewPager`监听与实际监听保持一致。

![效果图](https://github.com/danledian/TurnsDemo/blob/master/demo/demo.gif)

##Gradle

    dependencies {
        compile 'com.dld.library:turns:1.0.0'
    }

##Maven

    <dependency>
      <groupId>com.dld.library</groupId>
      <artifactId>turns</artifactId>
      <version>1.0.0</version>
      <type>pom</type>
    </dependency>

##使用介绍
* 重写`TurnsPagerAdapter`，并实现两个方法，`getItemCount`方法返回总个数，`instantiateTurnsItem`方法返回需要创建的`View`和对`View`进行初始化，注意：请勿使用类似`List<View>` `get`方式得到`View`，必须创建View即可，具体实现已经将View进行缓存，不会出现无限创建View情况，用法参照如下代码：

        @Override
        public int getItemCount() {
            return imageUrls.length;
        }

        @Override
        protected Object instantiateTurnsItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_advert, container, false);
            ImageView iv = (ImageView) view.findViewById(R.id.bg_iv);
            ImageLoader.load(MainActivity.this, imageUrls[position], iv);
            return view;
        }

* 初始化
	
        mCycleViewPager = (AutoCycleViewPager)findViewById(R.id.viewPager);
        mPageIndicator = (PageIndicator)findViewById(R.id.pageIndicator);
		
        mCycleViewPager.setAdapter(new BasePagerAdapter());
        mPageIndicator.setViewPager(mCycleViewPager);
		
		/* 开启自动播放 */
      	mCycleViewPager.startAutoCycle();

* 添加点击监听

        mCycleViewPager.setOnItemClickListener(new CycleViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });

* 修改指示器样式
`AutoCycleViewPager`与`PageIndicator`为单独两部分，如需更换`PageIndicator`，需修改`PageIndicator`中两点即可。

	* 指示器个数
	
        int count = ((TurnsPagerAdapter)mCycleViewPager.getAdapter()).getItemCount();
	* 指示器监听换成`OnCycleViewPageChangeListener`，用法与`ViewPager.OnPageChangeListener`一致
		
	        mCycleViewPager.setOnCycleViewPageChangeListener(new CycleViewPager.OnCycleViewPageChangeListener() {
	            @Override
	            public void onTurnsPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	                
	            }
	
	            @Override
	            public void onTurnsPageSelected(int position) {
	
	            }
	
	            @Override
	            public void onTurnsPageScrollStateChanged(int state) {
	
	            }
	        });

## License

MIT

