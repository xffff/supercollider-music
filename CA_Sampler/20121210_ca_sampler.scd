(
var ca, voices, speed;
voices = 50;
speed = 4;
ca = ModernLife.new(voices);
ca.fps_(speed);
ca.userFunc_({|states,population|
	var pitch, amp, rowcount, columncount;
	pitch = Array.newClear(voices);
	amp = Array.newClear(voices);

	for(0,voices,{|i|
		columncount[i] = (columncount[i] + states[i][i])
		for(0,voices,{|j|
			rowcount[i] = (rowcount[i] + states[i][j]);
		})
	})
});
)