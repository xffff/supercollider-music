(
var ca, voices, speed;
var averages;
var routine, playroutine;
var buffers, maxbuflen, numinchans;
var probrecplay;

voices = 50;
speed = 4;
ca = ModernLife.new(voices);
ca.fps_(speed);

averages = Array.fill(2,{Array.newClear(voices)});

maxbuflen = 5;
numinchans = 1;
buffers = Array.fill(voices,{Buffer.alloc(s,maxbuflen*s.sampleRate,numinchans)});

probrecplay = 0.5;

ca.userFunc_({|states,population|
	var columncount = Array.newClear(voices);
	var l_voices = voices-1;
	for(0,l_voices,{|i|
		var rowcount = Array.newClear(voices);
		for(0,l_voices,{|j|
			rowcount[i] = (rowcount[i] + states[i][j]);
		})
		// freq ratios
		averages[0][i] = rowcount/voices;
	});

	for(0,l_voices,{|i|
		for(0,l_voices,{|j|
			columncount[i] = (columncount[i] + states[j][i]);
		});
	});
	// amplitudes
	averages[1][i] = columncount/voices;
});

SynthDef(\recbuf, { |in = 0, dur = 1, bufnum = 0|
	var sig = In.ar(in,numinchans);
	Line.kr(1,0,dur,doneAction:2);
	RecordBuf.ar(sig,bufnum,loop:0,doneAction:2);
}).add;

SynthDef(\playbuf, { |out = 0, rate = 0, bufnum = 0, amp = 1, samplelen = 5, pan = 0, rampscale = 0.2|
	var dur = (samplelen / rate) * 0.95;
	var sound = PlayBuf.ar(1,bufnum,rate,loop:0,doneAction:2);
	var snum = 1e-3;
	var scaled = rampscale.clip(snum,1.0).linlin(snum,1.0,snum,(dur*0.5)-snum);
	sound = sound * EnvGen.kr(Env.linen(scaled,dur-(scaled*2),scaled,amp,\sin),doneAction:2);
	sound = PanAz.ar(numchans,sound,pan);
	OffsetOut.ar(out,sound);
}).add;

routine = {
	loop{

		speed.reciprocal.wait;
	};
};

playroutine = routine.fork;

)

