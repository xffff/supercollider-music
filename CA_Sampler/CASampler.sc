// Michael Murphy 2012

CASampler {
	var ca, numvoices, <>speed;
	var averages;
	var routine, playroutine;
	var buffers, <>maxbuflen, synths, numinchans, numoutchans, casynthgroup, <>busin, <>busout;
	var <>probrecplay, <>grainramp, <>maxamp, <>precision, <>ranges,<>minsamplelen,rules;
	var loopfunction, looproutine;
	var population;

	*new { |l_busin, l_busout,l_numvoices,l_maxbuflen,l_speed|
		^super.new.init(l_busin, l_busout,l_numvoices,l_maxbuflen,l_speed);
	}

	init { |l_busin, l_busout,l_numvoices,l_maxbuflen,l_speed|

		busin = l_busin;
		numinchans = busin.size.max(1);
		busout = l_busout;
		numoutchans = busout.size.max(1);
		maxbuflen = l_maxbuflen;
		numvoices = l_numvoices.max(1);
		maxamp = numvoices.reciprocal;
		speed = l_speed;
		averages = Array.fill(2,{Array.newClear(numvoices)});
		probrecplay = 0.5;
		precision = 1e-5;
		ranges = [-12,12];
		grainramp = 0.01;
		minsamplelen = 1/16;
		rules="23/3";

		ca = ModernLife.new(numvoices);
		ca.fps_(speed);
		ca.setRule(rules);

		buffers = Array.fill(numvoices,{
			Buffer.alloc(Server.default,maxbuflen*Server.default.sampleRate,numinchans)
		});
		synths = Array.fill(2,{Array.newClear(numvoices)});
		casynthgroup = Group.new;

		looproutine = Array.newClear(numvoices);
		loopfunction = Array.newClear(numvoices);

		ca.userFunc_({|states,l_population|
			var counts = Array.fill(2,{Array.fill(numvoices,{0})});
			population = l_population; // use this for amp scaling
			for(0,numvoices-1,{|i|
				for(0,numvoices-1,{|j|
					counts[0][i] = (counts[0][i] + states[i][j]); // rows
					counts[1][i] = (counts[1][i] + states[j][i]); // cols
				});
				averages[0][i]=counts[0][i]/numvoices; // averages of rows
				averages[1][i]=counts[1][i]/numvoices; // averages of cols
			});
		});

		this.loadsynths;
	}

	loadsynths {
		fork{
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
				sound = PanAz.ar(numoutchans,sound,pan);
				OffsetOut.ar(out,sound);
			}).add;
			Server.default.sync;
		};
	}

	play {
		numvoices.do{ |i|
			this.playlooproutine(i);
		}
	}

	playlooproutine { |i|
		loopfunction[i] = {
			// samplelen is a function of overall population density
			var samplelen = maxbuflen.linlin(0,population,maxbuflen,minsamplelen);
			loop{
				var waittime = speed.reciprocal;
				// record or playback dependant upon probability
				if(1.0.rand<probrecplay,{
					samplelen = maxbuflen.linlin(0,population,maxbuflen,minsamplelen);

					synths[0][i] = Synth(\recbuf,[
						\in, busin,
						\dur, samplelen,
						\bufnum, buffers[i]
					], casynthgroup, \addToHead);

					waittime = samplelen;
					},{
						var thisaverages = [averages[0][i], averages[1][i]];
						// only play if the cell is alive
						if(thisaverages[0]!=0||thisaverages[1]!=0,{
							var playrate = averages[0][i].linlin(0,1.0,
								ranges[0],ranges[1]).round(precision).midiratio;
							var amp = averages[1][i].linlin(0,numvoices,numvoices.reciprocal,maxamp);
							var pan = i.linlin(0,numvoices-1,-1.0,1.0);

							synths[1][i] = Synth(\playbuf, [
							\out, busout,
							\bufnum, buffers[i],
							\rate, playrate,
							\samplelen, samplelen,
							\amp, amp,
							\pan, pan,
							\rampscale, grainramp
							], casynthgroup, \addToTail);

							// scale waittime so can't record into the buffer while playing
							waittime = samplelen * playrate.reciprocal;
						});
					}
				);
				waittime.wait;
			};
		};
		looproutine[i] = loopfunction[i].fork;
	}

	setrules { |rulestring|
		rules = rulestring;
		ca.setRule(rules);
	}

	pause {
		numvoices.do{|i| looproutine[i].stop;} // this doesn't work... ?
	}

	unpause {
		numvoices.do{|i| looproutine[i] = loopfunction[i].fork;}
	}

	free {
		this.pause; // this doesn't work
		numvoices.do{|i|
			// because the synths have doneAction:2 set (they free themselves)
			// freeing them won't do squat if they aren't playing.
			1.do{|j| if(synths[j][i].isPlaying,{synths[j][i].free;})};
			buffers[i].free;
		};
		casynthgroup.free;
	}
}