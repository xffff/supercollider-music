/************************************	*/
/* Ensemble Piece		             	*/
/*	MAIN							*/
/************************************	*/

(
~cleanup = nil;
~main_function = nil;
~input = nil; 
~fx = nil; 
~output = nil; 
~master_fx_bus = nil; 
~buf_a = nil;
~dryaudio = nil;

fork{
	~cleanup = fork{
		postln("Cleaning up...");
		s.newAllocators;
		~input.free; ~fx.free; ~output.free;
		~master_fx_bus.free;
		~buf_a.free;
		~dryaudio.free;
		s.sync; 
		postln("Done cleaning up");
	};
	
	0.05.wait; // make sure we're cleaned up before we do the rest
	
	~main_function = fork{
		(
		// groups
		~input = Group.new(s,\addToHead);   // from sampler
		~fx = Group.new(s, \addToTail);     // fx chain
		~output = Group.new(s, \addToTail); // output
		postln("Groups Allocated");
		
		// busses
		~master_fx_bus  = Bus.audio(s,~numfxchans); // fx bus
		postln("Busses Set");
		
		// buffers
		~buf_a = Buffer.alloc(s, s.sampleRate * 12.5, 1);
		postln("Buffers Allocated");
		);
		
		////////////////////////////////////////////////////
		
		// persistent synthdefs
		(
		~dryaudio = Synth(\dryaudio, 
			[
			\in, [0,1,2,3,4,5,6,7], // this needs to be fixed it is not working properly
			\amp, 1.0, 
			\out, ~master_fx_bus,
			], target: ~input, addAction: \addToHead);
			
			postln("Dry Audio On");
			s.queryAllNodes;
		);
		
		
		// section lengths
		(
		~section1_dur = 60; // these should be converted to an array
		~section2_dur = 50;
		~section3_dur = 40;
		~section4_dur = 60;
		~section5_dur = 60;
		~section6_dur = 10;
		~section7_dur = 60;
		~section8_dur = 40;
		~section9_dur = 20;
		
		postln("Section Lengths");
		postln("________________");
		post("1: "); ~section1_dur.postln;
		post("2: "); ~section2_dur.postln;
		post("3: "); ~section3_dur.postln;
		post("4: "); ~section4_dur.postln;
		post("5: "); ~section5_dur.postln;
		post("6: "); ~section6_dur.postln;
		post("7: "); ~section7_dur.postln;
		post("8: "); ~section8_dur.postln;
		post("9: "); ~section9_dur.postln;
		);
		
		~pelog = Array.fill(10, {|i| Scale.pelog.degrees + (12*i) }).lace.sort;
		
		//////////////////////////////////////////////////// MAIN SECTIONS
		(
		
		(
		Pdef(\section1_drum1, Pfindur(~section1_dur, Pbind(
				\type, \midi,\midicmd, \noteOn, \midiout, ~mOut[0],	
				\chan, 0,
				\midinote, 
					Pif(Ptime(inf) < (~section1_dur*0.25), Prand([61],inf), 
						Pif(Ptime(inf) < (~section1_dur*0.25), Pseq([60,Pwrand([61,63,62,60],[5,5,1,1].normalizeSum,inf)],1),
						Pif(Ptime(inf) < (~section1_dur*0.25), Prand([61,63,70,62,60],inf),
						Pif(Ptime(inf) < (~section1_dur*0.25), Prand([61,63,65,67,66,62,70,60],inf),
							Prand([64,66],inf)
					)))),
				\dur, Pseq([1,Pwrand([1/4,1/2,1,Pn(1/8,2)],[0.9,0.1,0.05,0.01].normalizeSum,inf)],1), 
				\amp, 
					Pif(Pbinop('==', Pkey(\midinote), 60), Pexprand(0.75,1.0,inf),
						Pif(Pbinop('==', Pkey(\midinote), 70), Pexprand(0.75,1.0,inf),
						Pif(Pbinop('==', Pkey(\midinote), 66), Pexprand(0.75,1.0,inf), 
							Pwrand([0,Pexprand(0.5,1.0,1)],[1,10].normalizeSum,inf)))
					)
			), 0)
		);
		);
		
		(
		Pdef(\section1_fx1, Pfindur(~section1_dur, Pbind(
				\instrument, \pitchshift,	
				\group, ~fx,
				\addAction, 0, // head
				\in, ~master_fx_bus.subBus(0,1),
				\out, 8,
				\dur, ~section1_dur, \delta, ~section1_dur,
				\atk, ~section1_dur,
				\sus, 20,
				\rel, 15,
				\amp, 0.35,
				\ratio, {3.0.rand}.dup(3),
				\windowSize, 1.0,
				\timeDispersion, 1/8,
				\pitchDispersion, {3.0.rand}.dup(3),
				\bpfselect, 1,
				\bpfres, 0.8,
				\bpffreq, 700
			),0)
		);
		);
		
		(
		Pdef(\section1_fx2, Pfindur(~section1_dur, Pbind(
				\instrument, \bpf,	
				\group, ~fx,
				\addAction, 1, // tail
				\in, ~master_fx_bus.subBus(0,1),
				\out, 8,
				\dur, ~section1_dur, \delta, ~section1_dur,
				\atk, ~section1_dur,
				\sus, 20,
				\rel, 15,
				\amp, 1.0,
				\res, 0.8,
				\freq, 550
			),0)
		);
		);
		
		(
		Pdef(\section1_sy1, Pfindur(~section1_dur, Pbind(
				\instrument, \subsyn,	
				\group, ~fx,
				\out, 14,
				\dur, ~section1_dur, 
				\atk, ~section1_dur,
				\sus, 0.1,
				\rel, 30,
				\amp, 0.45,
				\decay, {10.0.rand}.dup(10),
				\freq, {~pelog[rrand(~pelog.size*0.5,~pelog.size)].midicps}.dup(10),
				\modmul, {(1..5).choose}.dup(10),
				\modfreq, {Array.series(10,0.1,0.01).choose}.dup(10),
				\filterselect, 1,
				\filterfreqstart, 100,
				\filterfreqend, 3000,
				\filterfreqtime, ~section1_dur*0.75
			),0)
		);
		);
		
		);
		////////////////////////////////////////////////////
		(
		
		(
		Pdef(\section2_drum1, Pfindur(~section2_dur, Pbind(
				\type, \midi,\midicmd, \noteOn, 
				\midinote, 
					Pseq([48,Pxrand([48,50,52,54,55,57,60,61,62,63,72,73,74,75],inf)],1).collect{ |note| 
						~midinote = note; note },
				\midiout, 
					Pif(Pkey(\midinote) <= 59, ~mOut[2], 
						Pif(Pkey(\midinote) <= 71, ~mOut[0],
							~mOut[1])),	
				\chan, 0,
				\dur, Pseq([4,
						Pif(Pbinop('==', Pkey(\midinote), 48), Pwrand([1,2,1/2],[1,0.5,1].normalizeSum,inf),
							Pif(Pbinop('==', Pkey(\midinote), 50), Pwrand([1,2,1/2],[1,0.5,1].normalizeSum,inf),
							Pif(Pbinop('==', Pkey(\midinote), 52), Pwrand([1,2,1/2],[1,0.5,1].normalizeSum,inf),
								Pwrand([1/2,1/4,1/8], [2,3,1].normalizeSum, inf)))
						)		
					],1).collect{|dur| ~duration = dur; dur }, 
				\amp, Pseq([1.0,
						Pif(Pbinop('==', Pkey(\midinote), 48), Pwrand([Pn(0,1),Pexprand(0.75,0.9,1)],[1,12].normalizeSum,inf),
							Pif(Pbinop('==', Pkey(\midinote), 50), Pwrand([Pn(0,1),Pexprand(0.75,0.9,1)],[1,12].normalizeSum,inf),
							Pif(Pbinop('==', Pkey(\midinote), 52), Pwrand([Pn(0,1),Pexprand(0.75,0.9,1)],[1,12].normalizeSum,inf),
								Pwrand([Pn(0,1),Pexprand(0.75,0.9,1)],[1,12].normalizeSum,inf)))
						)
					],1),
				\legato, Pif(Pbinop('==', Pkey(\midinote), 48), 4,
							Pif(Pbinop('==', Pkey(\midinote), 50), 4,
							Pif(Pbinop('==', Pkey(\midinote), 52), 4,
								Pkey(\dur)))
						)
			))
		);
		);
		
		
		( // -1 here to stop the pop in s3
		Pdef(\section2_fx1, Pfindur(~section2_dur, Pbind(
				\instrument, \pitchshift,
				\group, ~fx,
				\in, ~master_fx_bus.subBus(2,1),
				\out, 8,
				\atk, 0.1,
				\dur, ~section2_dur,
				\sus, ~section2_dur,
				\rel, 0.1,
				\amp, 1.0, // this was 2
				\ratio, 0.5
			))
		);
		);
		
		( // -1 here to stop the pop in s3
		Pdef(\section2_fx2, Pfindur(~section2_dur, Pbind(
				\instrument, \tri_pitchshift,
				\group, ~fx,
				\in, ~master_fx_bus.subBus(0,3),
				\out, 8,
				\dur, ~section2_dur,
				\atk, ~section2_dur,
				\sus, 0.1,
				\rel, 20,
				\amp, 1.5,
				\ratio, 2.0, 
				\windowSize, 1.1,
				\timeDispersion, 1.0.rand,
				\pitchDispersion, 1.0.rand
			))
		);
		);
		
		(
		Pdef(\section2_fx3, Pfindur(~section2_dur, Pbind(
				\instrument, \tri_freqshift,	
				\group, ~fx,
				\in, ~master_fx_bus.subBus(1,3),
				\out, 9,
				\dur, Pfunc{~duration},
				\sus, Pkey(\dur),
				\rel, Pkey(\dur)*2,
				\amp, Pexprand(0.3,0.95,inf), // decrescendo
				\freq, Prand(~pelog,inf).max(1).midicps,
				\lagtime, Prand([Pn(0,1),Prand([Pkey(\dur).rand,0],1)],inf), // can't decide... stochastic is good
				\decaytime, Pn(2.0.rand,inf),
				\filterswitch, 1,
				\cutoff, Pseg(Pexprand(500,3000,inf),Prand([1/4,1/8,1/2],inf),\exp,inf),
				\res, Pexprand(0.1,1.0,inf),
				\decaytime, Pexprand(1.0,2.5,inf)
			),0)
		);
		);
		
		); 
		////////////////////////////////////////////////////
		(
		(
		Pdef(\section3_drum1, Pfindur(~section3_dur, Pbind(
				\type, \midi,\midicmd, \noteOn, \midiout, ~mOut[2],	
				\chan, 0,
				\midinote, Pseq([Prand([48,50,52],1), Prand([48,50,52],inf)],1),
				\dur, Pseq([8,Pwrand([1,8,2,1/2],[4,1,4,1].normalizeSum,inf)],1).collect{|dur| 
						~duration = dur; dur }, 
				\amp, 
					Pseq([1.0,
						Pif(Pbinop('==',Pkey(\midinote),55),Pexprand(0.1,0.75,inf),
							Pexprand(0.1,1.0,inf))],1),
				\legato, 2
			))
		);
		);
		
		(
		Pdef(\section3_drum2, Pfindur(~section3_dur, Pbind(
				\type, \midi,\midicmd, \noteOn, \midiout, ~mOut[3],	
				\chan, 0,
				\midinote, 
					Ptuple([
						Pstutter(Prand([1,2,4],inf),Prand([40,42,44],inf),inf), // add some tuplets here
						Pstutter(Prand([1,2,4],inf),
							Pwrand([40,41,42,43,44,45],[2,1,2,1,2,1],inf),inf)		
					],inf),
				\dur, Pfunc{~duration}, 
				\amp, Pseq([1.0,Pexprand(0.1,1.0,inf)],1)
			))
		);
		);
		
		(
		Pdef(\section3_drum3, Pfindur(~section3_dur, Pbind(
				\type, \midi,\midicmd, \noteOn, \midiout, ~mOut[2],	
				\chan, 0,
				\midinote, Pwrand([48,50,52,55],[1,1,1,10].normalizeSum,inf),
				\dur, Pseq([8,Pwrand([1,8,2,1/2],[4,1,4,1].normalizeSum,inf)],1),
				\amp, Pstep([0,Pexprand(0.1,1.0,inf)],~section3_dur*0.25)
			))
		);
		);
		
		(
		Pdef(\section3_fx1, Pfindur(~section3_dur, Pbind(
				\instrument, \warpin,
				\group, ~fx,
				\in, ~master_fx_bus.subBus(3,1),
				\out, 10,
				\hpfreq, 20,
				\dur, ~section3_dur,
				\atk, 10,
				\sus, ~section3_dur-10,
				\amp, 0.5,
				\warpfactor, [Scale.pelog.degreeToRatio(3,-1),Scale.pelog.degreeToRatio(3,0)],
				\freqscale, [Scale.pelog.degreeToRatio(3,-1),Scale.pelog.degreeToRatio(3,0)]
			))
		);
		);
		
		(
		Pdef(\section3_fx2, Pfindur(~section3_dur, Pbind(
				\instrument, \pitchshift,	
				\group, ~fx,
				\in, ~master_fx_bus.subBus(3,1),
				\out, 8,
				\dur, ~section3_dur, \delta, ~section3_dur,
				\atk, 0.1,
				\sus, (~section3_dur*0.5),
				\rel, (~section3_dur*0.5),
				\amp, 0.6,
				\ratio, {2.0.rand}.dup(8),
				\windowSize, 2.1,
				\timeDispersion, {2.0.rand}.dup(8),
				\pitchDispersion, {2.5.rand}.dup(8)
			),0)
		);
		);
		
		(
		Pdef(\section3_sy1, Pfindur(~section3_dur, Pbind(
				\instrument, \subsyn,	
				\group, ~fx,
				\out, 14,
				\dur, ~section3_dur, 
				\atk, 0.1,
				\sus, 0.1,
				\rel, ~section3_dur*0.75,
				\amp, 1.0,
				\freq, {~pelog[rrand(~pelog.size*0.5,~pelog.size)].midicps}.dup(5),
				\modmul, {exprand(0.01,0.1)}.dup(5),
				\modfreq, {exprand(0.01,0.1)}.dup(5),
				\amselect, 1,
				\amfreq, (Scale.pelog.degrees+1).reciprocal,
				\amrel, {exprand(0.01,0.1)}.dup(5),
				\decay, 0.1
			),0)
		);
		);
		
		
		);
		////////////////////////////////////////////////////
		(
		(
		Pdef(\section4_drum1, Pfindur(~section4_dur, Pbind(
				\type, \midi,\midicmd, \noteOn, \midiout, ~mOut[3],	
				\chan, 0,
				\midinote, Pif(Ptime(inf) < (~section4_dur*0.5), Pwrand([46,47,40,42,44],[1,1,2,2,2].normalizeSum,inf),
							Prand([46,47,40,41,42,43,44,45],inf)),
				\dur, Prand([Pn(1/3,3),Pn(1/8,4),Pn(1/4,2),1/2],inf), 
				\amp, Pwrand([0,Pexprand(0.5,1.0,1)],[0.5,0.5],inf)
			))
		);
		);
		
		(
		Pdef(\section4_fx1, Pfindur(~section4_dur, Pbind(
				\instrument, \warpin,
				\group, ~fx,
				\in, ~master_fx_bus.subBus(3,1),
				\out, 10,
				\hpfreq, 20,
				\dur, ~section4_dur,
				\atk, ~section4_dur*0.5,
				\sus, ~section4_dur*0.5,
				\amp, 0.5,
				\warpfactor, [Scale.pelog.degreeToRatio(4,-1),Scale.pelog.degreeToRatio(4,0)],
				\freqscale, [Scale.pelog.degreeToRatio(4,-1),Scale.pelog.degreeToRatio(4,0)]
			))
		);
		);
		
		);
		////////////////////////////////////////////////////
		
		(
		(
		Pdef(\section5_drum1, Pfindur(~section5_dur, Pbind(
				\type, \midi,\midicmd, \noteOn, 	
				\chan, 0,
				\dur, 
					Pif(Ptime(inf) < (~section5_dur*0.5), Prand([1/8],inf), 
						Pwrand([Pn(1/8,8),Pseq([Pn(1/12,3),Pn(1/8,6)],1)],[0.9,0.1],inf)), 
				\midinote, 
					Pif(Pbinop('==', Pkey(\dur), 1/12), Pstutter(3,Prand([72,74,84,86],3)),
						Pif(Ptime(inf) < (~section5_dur*0.3), Pn(Pshuf([58,46,47,40,42,44],Prand((1..10),1)),inf),
						Pif(Ptime(inf) < (~section5_dur*0.3), Pn(Pshuf([58,46,47,40,42,44,72,74,84,86],Prand((1..10),1)),inf),
							Pxrand([58,40,41,42,43,44,45],inf)))),
				\midiout, 
					Pif(Pkey(\midinote) <= 47, ~mOut[3], 
						Pif(Pbinop('==', Pkey(\midinote), 58), ~mOut[4],
						Pif(Pkey(\midinote) <= 62, ~mOut[0],
							~mOut[1]))),	
				\amp, Pwrand([0,Pexprand(0.5,1.0,1)],[1,9].normalizeSum,inf)
			),0)
		);
		);
		
		(
		Pdef(\section5_fx1, Pfindur(~section5_dur, Pbind(
				\instrument, \pitchshift,	
				\group, ~fx,
				\in, ~master_fx_bus.subBus(1,1),
				\out, 8,
				\dur, ~section5_dur, 
				\atk, 0.01,
				\sus, ~section5_dur,
				\rel, 0.1,
				\amp, 0.6,
				\ratio, 1,
				\windowSize, 0.1,
				\timeDispersion, 0.0001,
				\pitchDispersion, 0.05
			),0)
		);
		);
		
		(
		Pdef(\section5_fx2, Pfindur(~section5_dur, Pbind(
				\instrument, \pitchshift,	
				\group, ~fx,
				\in, ~master_fx_bus.subBus(3,1),
				\out, 8,
				\dur, ~section5_dur, 
				\atk, ~section5_dur,
				\sus, 0.1,
				\rel, 10,
				\amp, 0.6,
				\ratio, 0.5,
				\windowSize, 0.1,
				\timeDispersion, 0.001,
				\pitchDispersion, 0.05
			),0)
		);
		);
		
		(
		Pdef(\section5_fx3, Pfindur(~section5_dur, Pbind(
				\instrument, \warpin,
				\group, ~fx,
				\in, ~master_fx_bus.subBus(3,1),
				\out, 10,
				\hpfreq, 20,
				\dur, ~section5_dur,
				\atk, ~section5_dur,
				\sus, 0.1,
				\rel, ~section5_dur,
				\amp, 0.5,
				\warpfactor, [Scale.pelog.degreeToRatio(4,-2),Scale.pelog.degreeToRatio(4,-1)],
				\freqscale, [Scale.pelog.degreeToRatio(4,-2),Scale.pelog.degreeToRatio(4,-1)]
			),0)
		);
		);
		
		
		);
		////////////////////////////////////////////////////
		(
		(
		Pdef(\section6_drum1, Pfindur(~section6_dur, Pbind(
				\type, \midi,\midicmd, \noteOn, 	
				\chan, 0,
				\midinote, Pxrand([41,43,45],inf), //Pn(Pshuf([41,43,45],Prand((1..10),1)),inf),
				\midiout, ~mOut[3],
				\dur, 1/4, 
				\amp, Pexprand(0.75,1.0,inf)
			))
		);
		);
		
		);
		////////////////////////////////////////////////////
		(
		(
		Pdef(\section7_drum1, Pfindur(~section7_dur, Pbind(
				\type, \midi,\midicmd, \noteOn, 	
				\chan, 0,
				\midinote, 
					Pif(Ptime(inf) < (~section7_dur*0.25), Pn(Pshuf([41,43,45],Prand((1..10),1)),inf), 
						Pif(Ptime(inf) < (~section7_dur*0.25), Pn(Pshuf([41,43,45,61,63],Prand((1..10),1)),inf),
						Pif(Ptime(inf) < (~section7_dur*0.25), Pxrand([41,43,45,55,57,61,63,73,75],inf),
							Pxrand([41,43,45,55,57,61,63,73,75,85,87],inf)))).collect{|note|
								~midinote = note; note }, // do this with pstep in future, no more ptime
				\midiout, 
					Pif(Pkey(\midinote) <= 10, ~mOut[5],
						Pif(Pkey(\midinote) <= 45, ~mOut[3], 
						Pif(Pkey(\midinote) <= 57, ~mOut[2],
						Pif(Pkey(\midinote) <= 63, ~mOut[0],
							~mOut[1])))),	
				\dur, Prand([1/4,1/2,1/8],inf), 
				\amp, Pexprand(0.75,1.0,inf)
			))
		);
		);
		
		(
		Pdef(\section7_drum2, Pfindur(~section7_dur, Pbind(
				\type, \midi,\midicmd, \noteOn, 	
				\chan, 0,
				\midinote, 
						Pif(Ptime(inf) < (~section7_dur*0.5), Pn(Pshuf([48,50,52],Prand((1..10),1)),inf),
						Pif(Ptime(inf) < (~section7_dur*0.25), Pxrand([10,48,50,52],inf),
							Pxrand([10,48,50,52],inf))).collect{|note|
								~midinote = note; note }, // do this with pstep in future, no more ptime
				\midiout, 
					Pif(Pkey(\midinote) <= 10, ~mOut[5],
						Pif(Pkey(\midinote) <= 45, ~mOut[3], 
						Pif(Pkey(\midinote) <= 57, ~mOut[2],
						Pif(Pkey(\midinote) <= 63, ~mOut[0],
							~mOut[1])))),	
				\dur, Prand([1,2,4,0.5],inf),
				\legato, 2, 
				\amp, Pstep([0,Pexprand(0.75,1.0,inf)], ~section7_dur*0.25, 1)
			))
		);
		);
		
		(
		Pdef(\section7_sy1, Pfindur(~section7_dur, Pbind(
				\instrument, \subsyn,	
				\group, ~fx,
				\out, 14,
				\dur, ~section7_dur, 
				\atk, ~section7_dur*0.5,
				\sus, ~section7_dur,
				\rel, ~section8_dur*0.75,
				\decay, 0.1,
				\amp, 1.5,
				\freq, {~pelog[exprand(~pelog.size*0.7,~pelog.size*0.9)].midicps}.dup(15), 
				\modmul, 0,
				\modfreq, {Array.series(10,0.1,0.01).choose}.dup(15),
				\amselect, 1,
				\amfreqline, 1,
				\amfreq, Array.fill(15, { |i| ~pelog[i]+1 }),
				\amfreqend, Array.fill(15, { |i| ~pelog[i]+1 }).reciprocal,
				\amfreqlinetime, ~section7_dur,
				\amenvrel, {exprand(0.01,0.1)}.dup(15),
			))
		);
		);
		
		
		(
		Pdef(\section7_fx1, Pfindur(~section7_dur, Pbind(
				\instrument, \quad_pitchshift,
				\group, ~fx,
				\in, ~master_fx_bus.subBus(0,4),
				\out, 8,
				\dur, Pn(~section7_dur*0.25,4), 
				\sus, ~section7_dur*0.5,
				\atk, ~section7_dur*0.5,
				\rel, 20,
				\amp, 6.0,
				\ratio, Prand([Prand(Scale.pelog.ratios,1),Prand(2-Scale.pelog.ratios,1)],inf),
				\windowSize, 4.0,
				\timeDispersion, Pexprand(0.01,Pseq(~pelog+1,inf),1),
				\pitchDispersion, Pexprand(0.01,Pseq(~pelog+1,inf),1)
			))
		);
		);
		
		(
		Pdef(\section7_fx2, Pfindur(~section7_dur, Pbind(
				\instrument, \warpin,
				\group, ~fx,
				\in, [~master_fx_bus.subBus(0,1),
						~master_fx_bus.subBus(1,1),
						~master_fx_bus.subBus(2,1),
						~master_fx_bus.subBus(3,1),
						~master_fx_bus.subBus(5,1),],
				\out, 10,
				\hpfreq, 10,
				\dur, ~section7_dur,
				\atk, ~section7_dur*0.5,
				\sus, ~section7_dur*0.5,
				\rel, ~section7_dur*0.5,
				\amp, 0.675,
				\warpfactor, Array.fill(4, { |i| Scale.pelog.ratios.reverse[i] }),
				\freqscale, Pkey(\warpfactor)
			))
		);
		);
		
		(
		Pdef(\section7_fx3, Pfindur(~section7_dur, Pbind(
				\instrument, \fbdelay,	
				\group, ~fx,
				\in, ~master_fx_bus.subBus(2,1),
				\out, 12,
				\dur, ~section7_dur, 
				\sus, ~section7_dur*0.5,
				\atk, 0.1,
				\rel, ~section7_dur*0.5,
				\maxdelay, Scale.pelog.degrees.choose.max(1),
				\delay, Pkey(\maxdelay),
				\fb, 0.99,
				\amp, 1.0,
				\filterselect, 1,
				\filterfreq, 750		
			), ~section8_dur)
		);
		);
		
		(
		Pdef(\section7_fx4, Pfindur(~section7_dur, Pbind(
				\instrument, \fbdelay,	
				\group, ~fx,
				\in, ~master_fx_bus.subBus(5,1),
				\out, 12,
				\dur, ~section7_dur, 
				\sus, ~section7_dur*0.5,
				\atk, 0.1,
				\rel, ~section7_dur*0.5,
				\maxdelay, {Scale.pelog.degrees.choose.max(1)}.dup(3),
				\delay, Pkey(\maxdelay),
				\fb, 0.99,
				\amp, 2.0
			), ~section8_dur)
		);
		);
		
		);
		////////////////////////////////////////////////////
		
		( // don't forget Array.interpolation!!! ///blendat ///
		(
		Pdef(\section8_drum1, Pfindur(~section8_dur, Pbind(
				\type, \midi,\midicmd, \noteOn, 	
				\chan, 0,
				\midinote, Pseq([40,42,44],inf),
				\midiout, ~mOut[3],
				\dur, 1/32, 
				\amp, Pseq([
					Pseq(Array.interpolation((~section8_dur*0.5)/(1/32),0.25,0.75),1),
					Pseq(Array.interpolation((~section8_dur*0.5)/(1/32),0.75,0.0),1),
					],1)
			))
		);
		);
		
		(
		Pdef(\section8_fx1, Pfindur(~section8_dur, Pbind(
				\instrument, \warpin,
				\group, ~fx,
				\in, [~master_fx_bus.subBus(0,1),~master_fx_bus.subBus(1,1),~master_fx_bus.subBus(3,1)],
				\out, 10,
				\hpfreq, 10,
				\dur, ~section8_dur,
				\atk, 0.1,
				\sus, ~section8_dur,
				\rel, 20,
				\amp, 0.5,
				\warpfactor, (Array.fill(6, { |i| ~pelog[i]+2 })++Array.fill(6, { |i| ~pelog[i]+2 }).reciprocal).sort,
				\freqscale, Pkey(\warpfactor)
			))
		);
		);
		
		(
		Pdef(\section8_fx2, Pfindur(~section8_dur, Pbind(
				\instrument, \fbdelay,	
				\group, ~fx,
				\in, ~master_fx_bus.subBus(3,1),
				\out, 12,
				\dur, ~section8_dur, 
				\sus, ~section8_dur,
				\atk, 0.1,
				\rel, 5,
				\maxdelay, Scale.pelog.degrees+1,
				\delay, Pkey(\maxdelay),
				\fb, {1.0.rand}.dup(5),
				\amp, {0.3.rand}.dup(5)
			))
		);
		);
		
		
		);
		////////////////////////////////////////////////////
		
		( 
		(
		Pdef(\section9_drum1, Pfindur(~section9_dur, Pbind(
				\type, \midi,\midicmd, \noteOn, 	
				\chan, 0,
				\midinote, 61,
				\midiout, ~mOut[0],
				\dur, ~section9_dur, 
				\amp, 0.5
			))
		);
		);
		
		);
		////////////////////////////////////////////////////
		
		postln("Sections Loaded");
	}; // end paren of main function
};
)